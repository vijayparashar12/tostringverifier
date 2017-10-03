# ToStringVerifier
#### if you are frustrated by every time your class coverage fails because of toString() method implementation.


```java
class Foo{
    private final String bar;
    private final String baz;
    Foo(String bar,String baz){
        this.bar = bar;
        this.baz = baz;
    }
    
    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
        .add("bar",bar);
    }
}

class FooTest {
    @org.junit.Test
    public void testConsiderAllFields(){
        Foo foo = new Foo(bar);
        tostringverifier.ToStringVerifier.forClass(Foo.class).with(foo).verify();
    }
    @org.junit.Test
    public void testExceptSpecificFiled(){
        Foo foo = new Foo(bar);
        tostringverifier.ToStringVerifier.forClass(Foo.class).with(foo).exceptFields("baz").verify();
    } 
    
    @org.junit.Test
    public void testWithSpecificFiled(){
        Foo foo = new Foo(bar);
        tostringverifier.ToStringVerifier.forClass(Foo.class).with(foo).withfields("bar").verify();
    }
}

```
        
