<h1 style="text-align: center;">Проект для самообучения “Обмен валют” </h1>
REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и
совершать расчёт конвертации произвольных сумм из одной валюты в другую.

Веб-интерфейс для проекта не подразумевается.

### Оглавление

* [Что нужно знать](#что-нужно-знать)
* [Мотивация проекта](#мотивация-проекта)
* [База данных](#база-данных)
  * [Таблица Currencies](#таблица-currencies)
  * [Таблица ExchangeRates](#таблица-exchangerates)
* [REST API](#rest-api)
  * [Валюты](#валюты)
  * [Обменные курсы](#обменные-курсы)
  * [Обмен валюты](#обмен-валюты)
* [Деплой](#деплой)
* [План работы над приложением](#план-работы-над-приложением)
* [Реализация](#реализация)
  * [Общие комментарии](#общие-комментарии)
  * [Требования и запуск приложения](#требования-и-запуск-приложения)
  * [Адрес деплоя(временный)](#адрес-деплоя-временный)

### Что нужно знать
* Java - коллекции, ООП
* Паттерн MVC(S)
* Maven/Gradle
* Backend
  * Java сервлеты
  * HTTP - GET и POST запросы, коды ответа
  * REST API, JSON
* Базы данных - SQLite, JDBC
* Деплой - облачный хостинг, командная строка Linux, Tomcat
* Фреймворки не используем.

### Мотивация проекта
* Знакомство с MVC
* REST API - правильное именование ресурсов, использование HTTP кодов ответа
* SQL - базовый синтаксис, создание таблиц

### База данных
В качестве базы данных предлагаю использовать SQLite. Это позволит встроить в ресурсы проекта файл с заполненными 
таблицами БД, что упростит деплой (детали ниже).

### Таблица `Currencies`
| Колонка  | Тип     | Комментарий                                |
|----------|---------|--------------------------------------------|
| ID       | int     | Айди валюты, автоинкремент, первичный ключ |
| Code     | Varchar | Код валюты                                 |
| FullName | Varchar | Полное имя валюты                          |
| Sign     | Varchar | Символ валюты                              |
Пример записи в таблице для австралийского доллара:

| ID | Code | FullName          | Sign |
|----|------|-------------------|------|
| 1  | AUD  | Australian dollar | A$   |
Коды валют мира - https://www.iban.com/currency-codes.

Индексы:

* Первичный ключ по полю ID
* Unique индекс по полю Code для гарантий уникальности валюты в таблице, и для ускорения поиска валюты по её 
аббревиатуре

### Таблица `ExchangeRates`
| Колонка          | Тип        | Комментарий                                                 |
|------------------|------------|-------------------------------------------------------------|
| ID               | int        | Айди курса обмена, автоинкремент, первичный ключ            |
| BaseCurrencyId   | int        | ID базовой валюты, внешний ключ на Currencies.ID            |
| TargetCurrencyId | int        | ID целевой валюты, внешний ключ на Currencies.ID            |
| Rate             | Decimal(6) | Курс обмена единицы базовой валюты к единице целевой валюты |

`Decimal(6)` - десятичное число с 6 знаками после запятой. Полезно для валют, отличающихся на порядки. Например, одна 
Японская иена равна 0.0073 USD.

Индексы:
* Первичный ключ по полю ID
* Unique индекс по паре полей BaseCurrencyId, TargetCurrencyId для гарантий уникальности валютной пары, и для ускорения 
поиска курса по паре валют

### REST API
Методы REST API реализуют CRUD интерфейс над базой данных - позволяют создавать (C - create), читать (R - read), 
редактировать (U - update). В целях упрощения, опустим удаление (D - delete).

### Валюты
**GET** `/currencies` \
Получение списка валют. Пример ответа:

```json
[
  {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },   
  {
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  }
]
```
HTTP коды ответов:
* Успех - 200
* Ошибка (например, база данных недоступна) - 500

**GET** `/currency/EUR` \
Получение конкретной валюты. Пример ответа:
```json
{
  "id": 0,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```
HTTP коды ответов:
* Успех - 200
* Код валюты отсутствует в адресе - 400
* Валюта не найдена - 404
* Ошибка (например, база данных недоступна) - 500

**POST** `/currencies` \
Добавление новой валюты в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Поля 
формы - `name`, `code`, `sign`. Пример ответа - JSON представление вставленной в базу записи, включая её ID:

```json
{
  "id": 0,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```
HTTP коды ответов:
* Успех - 201
* Отсутствует нужное поле формы - 400
* Валюта с таким кодом уже существует - 409
* Ошибка (например, база данных недоступна) - 500

### Обменные курсы
**GET** `/exchangeRates` \
Получение списка всех обменных курсов. Пример ответа:

```json
[
  {
    "id": 0,
    "baseCurrency": {
      "id": 0,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 1,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 0.99
  }
]
```
HTTP коды ответов:
* Успех - 200
* Ошибка (например, база данных недоступна) - 500

**GET** `/exchangeRate/USDRUB` \
Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Пример 
ответа:

```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```
HTTP коды ответов:
* Успех - 200
* Коды валют пары отсутствуют в адресе - 400
* Обменный курс для пары не найден - 404
* Ошибка (например, база данных недоступна) - 500

**POST** `/exchangeRates` \
Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). 
Поля формы - `baseCurrencyCode`, `targetCurrencyCode`, `rate`. Пример полей формы:

* `baseCurrencyCode` - USD
* `targetCurrencyCode` - EUR
* `rate` - 0.99

Пример ответа - JSON представление вставленной в базу записи, включая её ID:
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```
HTTP коды ответов:

* Успех - 201
* Отсутствует нужное поле формы - 400
* Валютная пара с таким кодом уже существует - 409
* Одна (или обе) валюта из валютной пары не существует в БД - 404
* Ошибка (например, база данных недоступна) - 500

**PATCH** `/exchangeRate/USDRUB`
Обновление существующего в базе обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. 
Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Единственное поле формы - `rate`.

Пример ответа - JSON представление обновлённой записи в базе данных, включая её ID:

```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```
HTTP коды ответов:

* Успех - 200
* Отсутствует нужное поле формы - 400
* Валютная пара отсутствует в базе данных - 404
* Ошибка (например, база данных недоступна) - 500

### Обмен валюты
**GET** `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT` \
Расчёт перевода определённого количества средств из одной валюты в другую. Пример запроса - 
**GET** `/exchange?from=USD&to=AUD&amount=10`.

Пример ответа:

```json
{
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Australian dollar",
    "code": "AUD",
    "sign": "A€"
  },
  "rate": 1.45,
  "amount": 10.00,
  "convertedAmount": 14.50
}
```
Получение курса для обмена может пройти по одному из трёх сценариев. Допустим, совершаем перевод из валюты **A** в 
валюту **B**:

1. В таблице `ExchangeRates` существует валютная пара **AB** - берём её курс
2. В таблице `ExchangeRates` существует валютная пара **BA** - берем её курс, и считаем обратный, чтобы получить **AB**
3. В таблице `ExchangeRates` существуют валютные пары **USD-A** и **USD-B** - вычисляем из этих курсов курс **AB**

Вопросы выгоды обменника и спреда зашитого в курс, убраны из ТЗ для упрощения. \
Более четко:
- если курс AB в базе есть, а BA - нет, то BA строго равен обратному курсу от AB
- если оба курса AB и BA существуют, они не обязаны зависеть друг от друга по формуле AB=1/BA

Остальные возможные сценарии, для упрощения, опустим.

Для всех запросов, в случае ошибки, ответ может выглядеть так:
```json
{
  "message": "Валюта не найдена"
}
```
Значение message зависит от того, какая именно ошибка произошла.

### Деплой
Будем вручную деплоить war артефакт в Tomcat, установленный на удалённом сервере. При использовании встроенной в 
проект SQLite базы данных, установка внешней SQL БД не требуется.

Шаги:

* Локально собрать war артефакт приложения
* В хостинг-провайдере по выбору арендовать облачный сервер на Linux
* Установить JRE и Tomcat
* Зайти в админский интерфейс Tomcat, установить собранный war артефакт

Ожидаемый результат - приложение доступно по адресу \
`http://$server_ip:8080/$app_root_path.`

### План работы над приложением
* Создать заготовку Java бэкенд приложения с `javax.servlet/jakarta.servlet`
* Создать таблицы в базе данных, и вручную их заполнить начальными данными (несколько валют, обменных курсов)
* Реализовать методы REST API для работы с валютами и обменными курсами
* Реализовать метод REST API с подсчётом обмена валюты
* Деплой на удалённый сервер

Источник [(Сергей Жуков)](https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/)

## Реализация
### Общие комментарии
Структура адресов проекта полностью соответствует указанной в ТЗ. Все endpoint-ы функционируют. База данных SQLite 
заполнена некоторыми начальными данными.  \
В корне репозитория находятся коллекция Postman для тестирования REST API для локального и удаленного сервера: \
CurrencyExchange.Remote.postman_collection.json \
CurrencyExchange.Locale.postman_collection.json
### Требования и запуск приложения
Для работы приложения необходима Java версии 21+. \
Поскольку приложение построено на сервлетах вам понадобится контейнер
сервлетов, такой как Tomcat, jetty и т.п. \
Необходимо собрать war файл из файлов проекта. Это можно сделать с помощью Maven, 
Ant. Многие IDE(например IntelliJ IDEA, Eclipse) упрощают этот процесс. \
С помощью Maven: запустить команду 'mvn package' в корневой папке проекта(на уровне с файлом pom.xml) для создания 
war файла (/target/CurrencyExchange-1.0.war). Затем передать war файл контейнеру сервлетов. 
В Tomcat есть графический интерфейс где можно указать путь до war файла.
### Адрес деплоя (временный)
http://82.146.33.42:8080/currency-exchange/