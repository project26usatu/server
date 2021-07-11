# server
## Калькулятор расчёта размера платы за электроснабжение (серверное приложение)
[![Build Status](https://project26.usatu.su/jenkins/buildStatus/icon?job=Project26_CID)](https://project26.usatu.su/jenkins/job/Project26_CID/)

## Описание репозитория

В данном репозитории располагается исходный код клиент-серверного веб-приложения «Калькулятор расчёта размера платы за электроснабжение».

### Содержание репозитория:
* Java EE backend-приложение, взаимодействующее с базой данных MySQL с одной стороны, REST API клиентами с другой стороны (директория [/src](/src))
* JavaScript frontend-приложение, позволяющие работать с backend-приложением через веб-браузер (директория [/www](/www))

## Описание приложения

Калькулятор расчёта размера платы за электроснабжение — это программа, которая используется для произведения автоматизированных расчётов суммы оплаты за поставленную электроэнергию. Она имеет интуитивно понятный интерфейс пользователя с реализацией возможности выбора различных категорий потребителей и режимов работы прибора учёта (одно- или многотарифный). Серверное приложение позволяет клиентам взаимодействовать с информацией, содержащейся в базе данных (получать, изменять, создавать и т. п.) посредством REST API или через веб-интерфейс.

## Рабочее окружение

* Контейнер сервлетов Java EE 8 Jetty 9.4.40
* Веб-сервер Nginx 1.20.0
* Сервер БД MySQL 8.0.24
* СУБД phpMyAdmin 5.1.0 (опционально)

## Установка
### Установка в Ubuntu/Debian:
Для установки требуется [Docker](https://docs.docker.com/engine/install/) и [Docker Compose](https://docs.docker.com/compose/install/). 

```console
curl -s https://project26.usatu.su/download/docker-deploy/0.2/script.sh | sudo bash
```

## Демо-версия
Ознакомиться с развёрнутым приложением можно [по ссылке](https://project26.usatu.su). Cookies и JavaScript должны быть включены.

## Дополнительные ссылки
* [Статус автоматического развёртывания в Jenkins](https://project26.usatu.su/jenkins/job/Project26_CID/)
* [Используемая модель тарифов и цен](https://www.bashesk.ru/consumer/become-a-customer/tariffs/current/)

## Лицензия
MIT, текст лицензии [по ссылке](LICENSE).