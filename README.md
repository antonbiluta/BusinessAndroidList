

<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/antonbiluta/BusinessAndroidList">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Индивидуальная задача по разработке мобильных приложений</h3>

  <p align="center">
    Билута. КубГУ ФКТиПМ 2022.
    <br />
    <a href="https://github.com/antonbiluta/BusinessAndroidList"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/antonbiluta/BusinessAndroidList">View Demo</a>
    ·
    <a href="https://github.com/antonbiluta/BusinessAndroidList/issues">Report Bug</a>
    ·
    <a href="https://github.com/antonbiluta/BusinessAndroidList/issues">Request Feature</a>
  </p>
</div>


----
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#о-проекте">О проекте</a>
      <ul>
        <li><a href="#технологии">Используемые технологии</a></li>
      </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>




<!-- ABOUT THE PROJECT -->
## О проекте
Андроид приложение, соответсвующее следующей задаче:
```
Список *[категорий]* (храниться отдельно) отражается в Navigation Drawer. После выбора категории отражается список *краткой информации* в виде всплывающего сообщения. Полную информацию об элементе отражать в окне детализации (список или не менее 8 полей). Информация хранится в БД SQLite и получается от внешнего сервера в формате JSON. Организовать сортировку элементов по атрибутам (долгое нажатие на соответствующем поле). Полную информацию об элементах загружать только в окно детализации.

1. [предприятия]
2. [список подразделений]
3. [название, адрес]
4. [количество сотрудников]
```

<p align="right">(<a href="#readme-top">Вверх</a>)</p>



### Технологии

* ServerApp - Java: Sockets, Gson, Json
* AndroidApp - Kotlin: Gson, SQLite



<!-- ROADMAP -->
## Roadmap

- [x] Сервер
    - [x] Объекты для распаковки
    - [x] Настройка сокетов
    - [x] Чтение и отправка JSON 
- [x] Андроид приложение
    - [x] Основное окно
    - [x] Меню
    - [x] Окно детализации
    - [x] Создать, Редактировать, Обновить, Удалить (CRUD)
    - [x] Всплывающие сообщения
    - [x] Дизайн
- [ ] Сдать преподавателю

<p align="right">(<a href="#readme-top">Вверх</a>)</p>



<!-- CONTRIBUTING -->
## Contributing / Вклад

Вклад - это то, что делает сообщество с открытым исходным кодом таким удивительным местом для обучения, вдохновения и творчества. Любой ваш вклад **высоко ценится**.

Если у вас есть предложение, которое сделало бы это лучше, пожалуйста, разветвите репозиторий и создайте запрос на извлечение. Вы также можете просто открыть проблему с тегом "улучшение".
Не забудьте дать проекту звезду! Еще раз спасибо!

1. Разветвите проект
2. Создайте свою ветку функций (`git checkout -b feature/AmazingFeature`)
3. Зафиксируйте свои изменения (`git commit -m 'Add some AmazingFeature'`)
4. Нажмите на ветку (`git push origin feature/AmazingFeature`)
5. Откройте запрос на слияние

<p align="right">(<a href="#readme-top">Вверх</a>)</p>



<!-- CONTACT -->
## Contact

Anton Biluta - [TgChannel](https://t.me/bilutachannel) - tosha@biluta.ru

Project Link: [https://github.com/antonbiluta/BusinessAndroidList](https://github.com/antonbiluta/BusinessAndroidList)

<p align="right">(<a href="#readme-top">Вверх</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/antonbiluta/BusinessAndroidList.svg?style=for-the-badge
[contributors-url]: https://github.com/antonbiluta/BusinessAndroidList/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/antonbiluta/BusinessAndroidList.svg?style=for-the-badge
[forks-url]: https://github.com/antonbiluta/BusinessAndroidList/network/members
[stars-shield]: https://img.shields.io/github/stars/antonbiluta/BusinessAndroidList.svg?style=for-the-badge
[stars-url]: https://github.com/antonbiluta/BusinessAndroidList/stargazers
[issues-shield]: https://img.shields.io/github/issues/antonbiluta/BusinessAndroidList.svg?style=for-the-badge
[issues-url]: https://github.com/antonbiluta/BusinessAndroidList/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/antonbiluta