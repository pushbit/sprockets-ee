Sprockets for Java EE [![Javadoc][javadoc-badge]][javadoc] [![Maven Central][maven-shield]][maven]
==================================================================================================

Create Servlets that automatically read and write JSON objects or Protocol Buffers messages.

Example
-------

Your Servlet will be sent an order in JSON and it will respond with a confirmation in JSON.

```java
public class OrderServlet extends JsonServlet<Order, Confirmation> {
    @Override
    protected Confirmation jsonPost(Order order,
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Confirmation confirmation = process(order);
        return confirmation;
    }
}
```

`WireServlet` follows the same pattern but reads and writes Protocol Buffers messages instead of JSON objects.

Install
-------

```xml
<dependency>
    <groupId>net.sf.sprockets</groupId>
    <artifactId>sprockets-ee</artifactId>
    <version>3.0.0</version>
</dependency>
```

[javadoc]: https://javadoc.io/doc/net.sf.sprockets/sprockets-ee/
[javadoc-badge]: https://javadoc.io/badge/net.sf.sprockets/sprockets-ee.svg
[maven]: https://search.maven.org/#search|ga|1|g%3Anet.sf.sprockets%20a%3Asprockets-ee
[maven-shield]: https://img.shields.io/maven-central/v/net.sf.sprockets/sprockets-ee.svg
