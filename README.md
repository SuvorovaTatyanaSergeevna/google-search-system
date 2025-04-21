# Автотесты для технического задания

Используется Selenide + JUnit 5 + Allure + AssertJ

### Структура проекта

``` gherkin
src
  + test
    + java
      + tests
            SearchInGoogleTest.java     Тестовый класс
      + ui
        + pages                         Пакет с page-классами
                ResultPage.java         
                StartPage.java    
        + steps
                GoogleSteps.java        Класс со step-методами
```

## Запуск тестов

#### Для запуска всех тестов

```shell
./gradlew clean test
````

#### Для запуска одного определенного тестового метода

```shell
./gradlew clean test --tests SearchInGoogleTest.название_тестового_метода
````

## Allure-отчет

#### Для генерации отчета ввести команду
```shell
./gradlew allureReport
```
Файл с отчётом находится в build/reports/allure-report/allureReport/index.html


## Особенности автотестов

ReCaptcha не автоматизирована.