# **SID2-PROJECT - TRACKADEMIC**

Este proyecto de **Sistemas de Datos Intensivos II** tiene como objetivo desarrollar una aplicación web que permita a los estudiantes gestionar sus notas y planes de evaluación de manera flexible y colaborativa.

Los estudiantes pueden registrarse, seleccionar o crear planes de evaluación por curso y semestre, ingresar sus notas y consultar el consolidado por periodo. También pueden editar o eliminar notas, modificar actividades de los planes verificando que sumen 100% y comentar sobre planes creados por otros usuarios. El sistema incluye al menos dos informes relevantes que ayudan a los estudiantes a tomar decisiones académicas basadas en su rendimiento.


El sistema está construido con una arquitectura que integra dos tipos de bases de datos:

- **PostgreSQL**: utilizada para almacenar información estructurada y relacional.
- **MongoDB**: empleada para manejar datos más flexibles.

El uso combinado de estas tecnologías permite optimizar tanto la integridad de los datos académicos como la flexibilidad requerida en escenarios colaborativos.


## **Integrantes del equipo**

| Nombre                    | Código      |
|---------------------------|-------------|
| Valentina Gomez           |  A00398790  |
| Alexis Delgado            |  A00399176  |
| Mariana De La Cruz        |  A00399618  |
| Juan Camilo Corrales      |  A00366910  |
| Corina Klinge             |  A00411745  |


## **Herramientas principales**

* **Spring Boot** – Framework principal para el backend
* **PostgreSQL** – Base de datos relacional para la información estructurada
* **MongoDB** – Base de datos NoSQL para los datos colaborativos y flexibles


## **Comando para Ejecutar el Proyecto**

```bash
./mvnw spring-boot:run
```

> Requisitos previos:
>
> * Tener **Maven** instalado (o usar el wrapper incluido `mvnw`)
> * Tener **Java 17 o superior**


