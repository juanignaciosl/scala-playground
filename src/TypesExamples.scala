import scala.collection.mutable.ArrayBuffer

object TypesPlayground {
  // References from https://speakerdeck.com/heathermiller/academese-to-english-a-practical-tour-of-scalas-type-system

  // Source: https://speakerdeck.com/heathermiller/academese-to-english-a-practical-tour-of-scalas-type-system
  object VarianceExample {
    trait Animal
    class Mammal extends Animal
    class Zebra extends Mammal

    object CovarianceExample {
      object InvariantRun {
        abstract class Run[A]

        class ZebraRun extends Run[Zebra]

        def isLargeEnough(run: Run[Mammal]): Boolean = ???

        // This won't compile because Field is defined as invariant
        // "You may wish to define A as +A instead. (SLS 4.5)"
        //isLargeEnough(new ZebraRun)
      }

      object CovariantRun {
        abstract class Run[+A]

        class ZebraRun extends Run[Zebra]

        def isLargeEnough(run: Run[Mammal]): Boolean = ???

        isLargeEnough(new ZebraRun)
      }
    }

    object ContravarianceExample {
      object InvariantVet {
        abstract class Vet[A]

        def treatMammals(vet: Vet[Mammal]) = ???

        class AnimalVet extends Vet[Animal]

        // This won't compile because Vet is defined as invariant
        // "You may wish to define A as -A instead. (SLS 4.5)"
        //treatMammals(new AnimalVet)
      }

      object CovariantVet {
        abstract class Vet[+A]

        def treatMammal(vet: Vet[Mammal]) = ???

        class ZebraVet extends Vet[Zebra]

        // This compile but meaning is wrong! A veterinary who can only treat Zebras
        // shouldn't be able to treat any Mammal!!!
        treatMammal(new ZebraVet)
      }

      object ContravariantVet {
        // Better definition: a vet is defined by the subtypes that he can treat, not supertypes!
        abstract class Vet[-A]

        def treatMammals(vet: Vet[Mammal]) = ???

        class AnimalVet extends Vet[Animal]

        treatMammals(new AnimalVet)

        class ZebraVet extends Vet[Zebra]
        // This won't compile and it's ok: with contravariance we can define that a Zebra vet
        // can't treat Mammals
        // treatMammals(new ZebraVet)
      }
    }

  }

  // Source: http://stackoverflow.com/questions/4531455/whats-the-difference-between-ab-and-b-in-scala
  object DifferenceBetweenVarianceAndBounds {
    class Animal
    class Dog extends Animal

    class Car
    class SportsCar extends Car

    // List is obviously not bounded
    val animals: List[Animal] = List(new Dog(), new Animal())
    val cars: List[Car] = List(new Car(), new SportsCar())

    // And since List defined with variance List[+B] A List[Dog] is also a List[Animal]
    case class Shelter(animals: List[Animal])
    val animalShelter: Shelter = Shelter(List(new Animal): List[Animal])
    val dogShelter: Shelter = Shelter(List(new Dog): List[Dog])

    // Bound example:
    case class Barn[A <: Animal](animals: A*)

    val animalBarn: Barn[Animal] = Barn(new Dog, new Animal)
    // This won't compile
    //val varBarn = Barn(new SportsCar)
  }

  object AbstractTypeMembersVsParametrization {
    trait Pet
    class Cat extends Pet

    object AbstractTypeMembers {
      class Person {
        type Pet
      }

      object Susan extends Person {
        type Pet = Cat
      }
    }

    object Parametrization {
      class Person[Pet]
      object Susan extends Person[Cat]
    }
  }

  object ExistentialTypes {
    object Description {
      case class Fruit[T](val tooRipe: T => Boolean)

      class Farm {
        // This won't compile
        // val fruit = new ArrayBuffer[Fruit[T]]

        // This does. We leave that part unknown but safe
        val fruit = new ArrayBuffer[Fruit[T] forSome { type T }]
      }
    }

    object Example {
      val strings = Array("a", "b")

      object InvarianceTrouble {
        def foo(x: Array[Any]) = println(x.length)
        // This won't compile because Array is invariant, although our code is safe. Alernatives?
        //foo(strings)
      }

      object TypeInferenceAlternative {
        def foo(x: Array[Any]) = println(x.length)
        // A) take advantage of type inference if possible
        foo(Array("a", "b"))
      }

      object TypeParametersAlternative {
        // B) declare with type parameters
        def foo[T](x: Array[T]) = println(x.length)
        foo(strings)
      }

      object ExistentialsAlternative {
        // C) use existentials
        def foo(x: Array[T] forSome { type T }) = println(x.length)
        foo(strings)
      }

      object ExistentialsShorthandAlternative {
        // C) use existentials shorthand
        def foo(x: Array[_]) = println(x.length)
        foo(strings)
      }
    }
  }
}