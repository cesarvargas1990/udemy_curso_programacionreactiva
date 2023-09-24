Generar .jars con intelij idea

ejecutar con diferentes puertos asi:

en la carpeta target donde este el .jar

ejecutar comando:    SERVER_PORT=8001 java -jar nombre_jar_generado.jar

de esta forma se pasan parametros al jar y se puede cambiar por otros puertos  8001,8002 , xxx 

ejecute los jar en otros puertos para probar las instancias correctas
en los proyectos estan las rutas de los endpoints

son basicas de POST,GET,PUT,DELETE

Ejemplo de json: que se usa en crear, editar
campo createAt se establece automaticamente si no se envia


{
    "nombre": "Apple iphone 12 mini reacondicionado",
    "precio": 450.0,
    "categoria": {
        "id": "650e1ac7692965f46f027f40",
        "nombre": "Electrónico"
    }
}


Curl de ejemplo:

curl --location --request GET 'localhost:8560/api/client/' \
--header 'Content-Type: application/json' \
--data '{
    "nombre": "Apple iphone 12 mini reacondicionado",
    "precio": 450.0,
    "categoria": {
        "id": "650e1ac7692965f46f027f40",
        "nombre": "Electrónico"
    }
}'

mirar los ejemplos y codigo para realizar las otras peticiones y el ejemplo de subir archivos

la carpeta sola: spring-boot-webflux tiene el ejemplo basico con un pequeño front
