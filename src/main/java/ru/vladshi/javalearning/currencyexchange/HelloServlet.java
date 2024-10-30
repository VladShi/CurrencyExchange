package ru.vladshi.javalearning.currencyexchange;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", value = "")
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("""
        <html><body>
            <h2>Привет Пейдж</h2>
            <h3>Старницы проетка №3 "Меняй деньга"</h3>
            <a href='https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/'>
            Ссыль на ТЗ. </a>
            Благодарности Сергею Жукову.
            <h4>Это api сервис, все ответы на запросе приходят в виде Json, так что тут не предусмотрен frontend <br>
            и всё будет страшно некрасиво. Этой страницы тут вообще не должно быть и сюда не надо ходить. <br>
            Оставлено для тех кому лень скачивать коллекции запросов для Postman, но хочется что-то потыкать.</h4>
            <p>Получение списка всех валют: <br><a href='currencies'>/currencies </a></p>
            <p>Получение конкретной валюты: <br><a href='currency/RUB'>/currency/RUB</a></p>
            <p>Получение списка всех обменных курсов: <br><a href='exchangeRates'>/exchangeRates</a></p>
            <p>Получение конкретного обменного курса: <br><a href='exchangeRate/USDRUB'>/exchangeRate/USDRUB</a></p>
            <p>Расчёт перевода определённого количества средств из одной валюты в другую: <br>
            <a href='exchange?from=USD&to=RUB&amount=10'>/exchange?from=USD&to=RUB&amount=10</a></p>
            <br> Для отправки остальных методов используйте POSTMAN или что-то подобное.
            <p>POST <b>/currencies</b> : <br>
            Добавление новой валюты в базу. Данные передаются в теле запроса в виде полей формы
            (x-www-form-urlencoded).
            <br>Поля формы - <br><b>name</b><br><b>code</b><br><b>sign</b></p>
            <p>POST <b>/exchangeRates</b> : <br>
            Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы
            (x-www-form-urlencoded).
            <br>Поля формы - <br><b>baseCurrencyCode</b><br><b>targetCurrencyCode</b><br><b>rate</b></p>
            <p>PATCH <b>/exchangeRate/USDRUB</b> :<br>
            Обновление существующего в базе обменного курса. В теле запроса в виде полей формы
            (x-www-form-urlencoded).
            <br>Единственное поле формы - <br><b>rate</b></p>
        </body></html>
        """);
    }
}