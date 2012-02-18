# What does Please do?

Sometimes we all want to touch eachothers' private parts. Java will let us, if we ask nicely. `Please` is a fledgling code-gen library that adds type-safety and convenience to the introspection operations required to gain access to private methods:

    Foo foo = new Foo(); 
    foo.somePrivateMethod(...)                // Compile error.

    
    Please please = new Please();
    please.call(foo).somePrivateMethod(all, the, usual, arguments);  // Success.

# How can I make it work for me?

Just download the `.jar`, as well as the dependencies (currently just javassist and annovention: two standalone `.jars`).

## Eclipse users ##

Add all three as references on your project's Build Path. Right-click on your project, and hit "Run as Java Application." Select the `Generate` entry point from Please's `jar`,
and you're away. Just add the newly-created `gen` folder as a `Class Folder` in your Build Path. Add the `@Expose` annotation to a class and re-run `Generate` to have it
generator the appropriate introspections; you must to this with every new class you want to use `Please` with.

## Non-eclipse users ##

I imagine you can just add the appropriate `jar`s to your classpath in `ant` and you're away. If someone can let me know how this works, I'd be very greatful (and will 
update the documentation).
