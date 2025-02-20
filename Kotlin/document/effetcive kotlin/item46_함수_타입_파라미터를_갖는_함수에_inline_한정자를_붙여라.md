### 함수 타입 파라미터를 갖는 함수에 inline 한정자를 붙여라

* 코틀린의 고차 함수(함수를 파라미터로 받는 함수 또는 함수를 리턴하는 함수)는 대부분 inline 한정자가 붙어있다.
* inline 한정자의 역할은 컴파일 시점에 '함수를 호출하는 부분'을 '함수의 본문'으로 대체한다.
* 일반적인 함수를 호출하면 함수 본문으로 점프하고, 본문의 모든 문장을 뒤에 호출했던 위치로 점프하는 과정을 거친다. 
* 하지만 inline 한정자를 사용해 함수를 호출하는 부분을 함수의 본문으로 대체하면 이러한 점프가 일어나지 않는다.

* 아래와 같은 함수가 있다고 가정하자.

```kotlin
fun doubleNumber(num: Int, doSomething: () -> Unit): Int {
    doSomething()
    return 2 * num
}

fun main() {
    val result = doubleNumber(10) {
        println("This is someMethod")
    }
    println(result)
}
```

* 이러한 코드의 경우 컴파일 시 어떠한 Java 코드로 변환하면 다음과 같다.

```java
public final class MainKt {
    public static final int doubleNumber(int num, @NotNull Function0 doSomething) {
        Intrinsics.checkNotNullParameter(doSomething, "doSomething");
        doSomething.invoke();
        return 2 * num;
    }

    public static final void main() {
        int result = doubleNumber(10, (Function0)null.INSTANCE);
        System.out.println(result);
    }

    // $FF: synthetic method
    public static void main(String[] var0) {
        main();
    }
}

```

* doSomething 에 람다식을 전달해주도록 구현한 부분이 새로운 객체를 생성하여 넘겨주는 식으로 변환되는 것을 확인할 수 있다. 
* 그리고 넘긴 객체를 통해 함수 호출을 하도록 구현되어 있다. 
* 이렇게 되면 무의미하게 객체를 생성하여 메모리를 차지하고, 내부적으로 연쇄적인 함수 호출을 하게 되어 오버헤드가 발생하여 성능이 떨어질 수 있다.
* doubleNumber 메서드에 inline 한정자를 사용하고 Decompile 하면 다음과 같이 변한다.

```java
public final class MainKt {
   public static final int doubleNumber(int num, @NotNull Function0 doSomething) {
      int $i$f$doubleNumber = 0;
      Intrinsics.checkNotNullParameter(doSomething, "doSomething");
      doSomething.invoke();
      return 2 * num;
   }

   public static final void main() {
      int num$iv = 10;
      int $i$f$doubleNumber = false;
      int var3 = false;
      String var4 = "This is someMethod";
      System.out.println(var4);
      int result = 2 * num$iv;
      System.out.println(result);
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }
}
```

* 람다를 호출하는 부분에 람다식 내부의 코드가 그대로 복사된 것을 확인해볼 수 있다. 
* 컴파일되는 바이트코드 양은 더 늘어나겠지만, 객체를 생성하거나 함수를 또 호출하는 등 비효율적인 행동은 하지 않는다.
* 이러한 이유로 인라인 함수는 일반 함수보다 성능이 좋다.

* 참고
  * 기본적으로 JVM 의 JIT 컴파일러에 의해서 일반 함수들은 inline 함수를 사용했을 때 더 좋다고 생각되어지면 JVM 이 자동으로 만들어주고 있다.
  * private 접근자일 경우 사용이 불가능 하다.
  * 너무 긴 메소드에 사용시 바이트 코드 길이가 더 길어져 메모리가 낭비 될 수있다.
  * inline keyword 는 1~3줄 정도 길이의 함수에 사용하는 것이 효과적일 수 있습니다.
  * 필요시 특정 메소드를 인라인 방식에서 제외 하고 싶다면 noinline 을 사용하자.


* inline 한정자를 사용하면 다음과 같은 장점이 있다.
  * 타입 아규먼트에 reified 한정자를 붙여서 사용할 수 있다.
  * 함수 타입 파라미터를 가진 함수가 훨씬 빠르게 동작한다.
  * 비 지역(non-local) 리턴을 사용할 수 있다.

### 타입 아규먼트를 reified 로 사용할 수 있다.

* JVM 바이트 코드 내부에는 제네릭이 존재하지 않아 컴파일을 하면 제네릭 타입과 관련된 내용이 제거된다.
* 그래서 객체가 List 인지 확인은 할 수 있어도 List<Int>인지 확인하는 코드는 사용할 수 없다.
* 같은 이유로 타입 파라미터에 대한 연산도 오류가 발생한다.

```kotlin
fun <T> printTypeName() {
    print(T::class.simpleName) // 오류
}
```

* 함수를 인라인으로 만들면 이러한 제한을 무시할 수 있다. 
* 함수 호출이 본문으로 대체되므로 reified 한정자를 지정하면 타입 파라미터를 사용한 부분이 타입 아규먼트로 대체된다.
* 컴파일하는 동안 printTypeName 의 본문이 실제로 대체되므로 실제로는 아래와 같이 된다.

```kotlin
inline fun <reified T> printTypeName() {
    print(T::class.simpleName)
}

fun main() {
    printtypeName<Int>()
    printtypeName<Char>()
    printtypeName<String>()
}
```

```java
public final class MainKt {
    public static final void main() {
        int $i$f$printTypeName = false;
        String var1 = Reflection.getOrCreateKotlinClass(Integer.class).getSimpleName();
        System.out.print(var1);
        $i$f$printTypeName = false;
        var1 = Reflection.getOrCreateKotlinClass(Character.class).getSimpleName();
        System.out.print(var1);
        $i$f$printTypeName = false;
        var1 = Reflection.getOrCreateKotlinClass(String.class).getSimpleName();
        System.out.print(var1);
    }
}
```

### 함수 타입 파라미터를 가진 함수가 훨씬 빠르게 동작한다.

* 모든 함수는 inline 한정자를 붙이면 조금 더 빠르게 동작한다. 
* 함수 호출과 리턴을 위해 점프하는 과정과 백스택을 추적하는 과정이 없기 때문이다.
* 그래서 표준 라이브러리의 간단한 함수들에는 대부분 inline 한정자가 붙어있다.
* 하지만 함수 파라미터를 가지지 않는 함수에는 이러한 차이가 큰 성능 차이를 발생시키지 않는다.

* inline 한정자를 사용하고 안하고의 성능 차이는 지역 변수를 캡처할 때 더 도드라지게 나타난다.

```kotlin
inline fun repeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun nonInlineRepeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun main() {
    var element1 = 0L
    repeat(10) {
        element1 += it
    }

    var element2 = 0L
    nonInlineRepeat(10) {
        element2 += it
    }
}
```

* inline 을 사용하면 지역변수 element 를 그대로 사용한다.
* 하지만, inline 을 사용하지 않으면 지역변수를 사용할 수 없고, 레퍼런스 객체로 래핑되고 내부적으로 사용한다.

```java
public final class MainKt {
   public static final void repeat(int times, @NotNull Function1 action) {
      int $i$f$repeat = 0;
      Intrinsics.checkNotNullParameter(action, "action");
      int index = 0;

      for(int var4 = times; index < var4; ++index) {
         action.invoke(index);
      }

   }

   public static final void nonInlineRepeat(int times, @NotNull Function1 action) {
      Intrinsics.checkNotNullParameter(action, "action");
      int index = 0;

      for(int var3 = times; index < var3; ++index) {
         action.invoke(index);
      }

   }

   public static final void main() {
      long element1 = 0L;
      int times$iv = 10;
      int $i$f$repeat = false;
      int index$iv = 0;

      for(byte var3 = times$iv; index$iv < var3; ++index$iv) {
         int var5 = false;
         element1 += (long)index$iv;
      }

      final Ref.LongRef element2 = new Ref.LongRef();
      element2.element = 0L;
      nonInlineRepeat(10, (Function1)(new Function1() {
         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object var1) {
            this.invoke(((Number)var1).intValue());
            return Unit.INSTANCE;
         }

         public final void invoke(int it) {
            Ref.LongRef var10000 = element2;
            var10000.element += (long)it;
         }
      }));
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }
}
```

### 비지역적 리턴을 사용할 수 있다.

```kotlin
inline fun repeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun nonInlineRepeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun main() {
    var element1 = 0L
    repeat(10) {
        element1 += it
        return
    }

    var element2 = 0L
    nonInlineRepeat(10) {
        element2 += it
        return // 오류 발생 !
    }
}
```

* inline 을 사용하지 않으면 내부에서 리턴을 사용할 수 없다.
* 이는 함수 리터럴이 컴파일 될 때, 함수가 객체로 매핑되어서 발생하는 문제인데, 함수가 외부에 있으므로 return 을 사용해서 main 으로 돌아올 수가 없다.
* 다만, inline 은 함수 내부에 존재하므로 이런 제한이 없다.

### inline 한정자의 비용

* inline 한정자는 재귀적으로 동작할 수 없다.
* 재귀적으로 사용하면 무한하게 대체되는 문제가 발생한다. 이는 인텔리제이가 잡아주지 못해서 굉장히 위험하다.

```kotlin
inline fun recursive(action: () -> Unit) {
    recursive2(action)
}

inline fun recursive2(action: () -> Unit) {
    recursive3(action)
}

inline fun recursive3(action: () -> Unit) {
    recursive(action)
}
```

```java
public final class MainKt {
   public static final void recursive(@NotNull Function0 action) {
      int $i$f$recursive = 0;
      Intrinsics.checkNotNullParameter(action, "action");
      int $i$f$recursive2 = false;
      int $i$f$recursive3 = false;
      throw new UnsupportedOperationException("Call is part of inline cycle: recursive(action)");
   }

   public static final void recursive2(@NotNull Function0 action) {
      int $i$f$recursive2 = 0;
      Intrinsics.checkNotNullParameter(action, "action");
      int $i$f$recursive3 = false;
      int $i$f$recursive = false;
      throw new UnsupportedOperationException("Call is part of inline cycle: recursive2(action)");
   }

   public static final void recursive3(@NotNull Function0 action) {
      int $i$f$recursive3 = 0;
      Intrinsics.checkNotNullParameter(action, "action");
      int $i$f$recursive = false;
      int $i$f$recursive2 = false;
      throw new UnsupportedOperationException("Call is part of inline cycle: recursive3(action)");
   }
}
```

* 인라인 함수는 가시성 제한을 가진 요소를 사용할 수 없다. -> public 인라인 함수 내부에서 private / internal 가시성을 가진 함수와 프로퍼티를 사용할 수 없다.
* 너무 긴 메소드에 사용시 바이트 코드 길이가 더 길어져 메모리가 낭비 될 수있다.

### crossinline 과 noninline

* 함수를 인라인으로 만들고 싶지만 어떤 이유로 일부 함수 타입 파라미터는 inline 으로 받고 싶지 않은 경우에 다음과 같은 한정자를 사용한다.
* crossinline : 아규먼트로 인라인 함수를 받지만, 비지역적 리턴을 하는 함수는 받을 수 없게 만든다. 인라인으로 만들지 않은 다른 람다 표현식과 조합해서 사용할 때 문제가 발생하는 경우 활용한다.
* noineline : 아규먼트로 인라인 함수를 받을 수 없게 만든다. 인라인 함수가 아닌 함수를 아규먼트로 사용하고 싶을 때 활용한다.

```kotlin
inline fun repeat(times: Int, crossinline action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun main() {
    var element1 = 0L
    repeat(10) {
        element1 += it
        return // 오류 발생 !
    }
}
```