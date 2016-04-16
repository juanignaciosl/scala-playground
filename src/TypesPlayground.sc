object TypesPlayground {

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
}