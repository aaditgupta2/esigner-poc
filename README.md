# esigner-poc
Simple poc to scrap one pdf from dealhub and esigned by docusigned api.

<b>Changes to be made:</b>
1. Change mysql db config in application.properties
2. Change file download location in application.properties
3. Change authToken in docsign.properties
4. Uncomment #spring.jpa.hibernate.ddl-auto = create in application.properties to create initial table and after that please uncomment.

<b>Steps to deploy:</b><br/>
1. Make sure mysql db is running and have initila schema "esigner"
2. RUn the spring boot app.
3. Access the app via : http:localhost:8080