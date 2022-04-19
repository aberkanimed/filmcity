# FilmCity Backend

> Estamos en el año 2021, los videoclubs del mundo se están extinguiendo, pero tus amig@s y tú, como grandes amantes del cine, habéis decidido crear vuestro propio videoclub privado: FilmCity.
Sois parte del equipo de Back de FilmCity y tenéis que hacer **el código del backend** de la aplicación. El equipo de Frontend es muy celoso con su trabajo, y os han pedido que NO toquéis el frontend para nada. Tendréis acceso al frontend para poder hacer pruebas lo antes posible.
¡Suerte!
>

## 🔧 Competencias técnicas

- Desarrollar la parte de backend (adaptar)

## 💻 Tecnologías

- Git / Github
- Java
- Spring Boot
- JSON


### API REST

Desde el equipo de frontend nos pasan una API que tenemos que implementar

| Method | Path | Request Body                                                                                               | Descripción | Response Body                                                                                                                                                                                                      |
| --- | --- |------------------------------------------------------------------------------------------------------------| --- |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET | /movies | -                                                                                                          | devuelve todas las películas | Lista de películas                                                                                                                                                                                                 |
| POST | /movies | { "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String }             | añade una película | La película que se ha creado. ha de incluir un ID autogenerado:                                                                                                                                                    { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String } |
| PUT | /movies | { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String } | modifica una película | La película modificada                                                                                                                                                                                             { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String } |
| DELETE | /movies/{id} | -                                                                                                          | borra la película que tenga el id {id} | La película que hemos borrado                                                                                                                                                                                      { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String } |
| PUT | /movies/{id}/book?renter={name} | -                                                                                                          | Marca como alquilada la película con el id {id} y registra el nombre de quien la ha alquilado | La película modificada { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String, "booked": true, "renter": {name} } (puede modificar la estructura Movie anterior) |
| PUT | /movies/{id}/return | -                                                                                                          | Marca como disponible una película | La película modificada { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String, "booked": false, "renter": null }                                                 |
| PUT  | /movies/{id}/rating | { "score": {0-5} } example: { "score": 3 }                                                                 | Valora la película con un número del uno al 5  | La película modificada { "id": Long, "title": String, "coverImage": String, "director": String, "year": int, "synopsis": String, "rating": int }                                                                   |

## Setup
* Clonar
* Abrir el proyecto en IntelliJ IDEA
* Ejecutar el servidor tomcat