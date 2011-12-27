Sometimes we all want to touch eachothers' private parts. Java will let us, if we ask nicely. `Please` is a fledgling code-gen library that adds type-safety and convenience to the introspection operations required to gain access to private methods:

    Foo foo = new Foo(); 
    foo.somePrivateMethod(...)                // Compile error.

    
    Please please = new Please();
    please.call(foo).somePrivateMethod(all, the, usual, arguments);  // Success.
