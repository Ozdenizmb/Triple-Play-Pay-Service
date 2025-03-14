# Payment

How to start the Payment application
---

1. Run `./gradlew clean build` to build your application
1. Start application with `java -jar build/libs/Payment-1.0-SNAPSHOT.jar server build/resources/main/application-local.yml`
1. To check that your application is running enter url `http://localhost:8080`

<br><br>

<h3>To ensure the project runs successfully, create a file named gradle.properties in the root directory and define the following properties in that file.</h3>

<p>triplePlayPayId=&lt;YOUR_TRIPLE_PLAY_PAY_ID&gt;</p>
<p>triplePlayPayApiKey=&lt;YOUR_TRIPLE_PLAY_PAY_API_KEY&gt;</p>
<p>triplePlayPayPublicApiKey=&lt;YOUR_TRIPLE_PLAY_PAY_PUBLIC_API_KEY&gt;</p>