# jLox

Java Interpreter for the Language Lox

## About the Project

This project is an exploration of interpreter development, 
inspired by the educational journey presented in "Crafting Interpreters."
The goal is to provide a practical implementation of a scripting language interpreter in Java, 
offering developers an opportunity to delve into the inner workings of language processing.

## The Lox Language

Lox is a dynamically-typed scripting language designed by Robert Nystrom, 
specifically for educational purposes in the "Crafting Interpreters" book. 
It is intentionally kept simple to make it easier to understand the concepts of interpreter development. 
Here are some key features of the Lox language:

### Variables

```
var x = 10;
var message = "Hello, Lox!";
```

### Control Structures
```
var num = 42;

if (num > 0) {
  print "Positive!";
} else {
  print "Non-positive!";
}

while (num > 0) {
  print num;
  num = num - 1;
}
```

### Functions
```
fun greet(name) {
  return "Hello, " + name + "!";
}

var greeting = greet("World");
print greeting;
```

### Classes
```
class Dog {
  var name;

  init(name) {
    this.name = name;
  }

  // Method to bark
  bark() {
    print this.name + " says Woof!";
  }
}

// Creating an instance of the Dog class
var myDog = Dog("Buddy");

// Accessing the properties and methods of the Dog instance
print "My dog's name is: " + myDog.name;
myDog.bark();
```