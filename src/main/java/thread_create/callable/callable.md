实现过程
（1）创建类实现Callable接口，并实现接口中的call（）方法，该call（）方法作为线程的执行体，并且有返回值。
（2）创建main（）主线程，并在main（）方法中new Callable实现类的实例，使用FutureTask类包装Callable对象，FutureTask对象封装了该Callable对象的call（）方法的返回值。
（3）使用创建的FutureTask对象实例作为Thread对象的参数，并启动线程。
（4）调用FutureTask对象的get（）方法来获得子线程执行结束后的返回值。



