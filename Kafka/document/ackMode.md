## AckMode

* Spring Kafka 에서는 auto commit 이 true 이지만, 수동 커밋으로 변경할 수 있다.
* 이 때, AckMode 를 설정해 줄 수 있는데, 종류는 아래와 같다.

* RECORD
  * 리스너가 레코드를 처리한 후 반환할 때 오프셋을 커밋한다.
* BATCH
  * poll()에서 반환된 모든 레코드가 처리되면 오프셋을 커밋한다.
  * 스프링 카프카 컨슈머의 AckMode 기본값
* TIME
  * poll()이 반환한 모든 레코드가 처리되면 마지막 커밋 이후 ackTime 이 초과되지 않는 한 오프셋을 커밋한다.
  * AckTime 옵션을 설정해야한다.
* COUNT
  * poll()에서 반환된 모든 레코드가 처리되면, 마지막 커밋 이후 ackCount 레코드가 수신된 경우에만 오프셋을 커밋한다.
  * AckCount 옵션을 설정해야한다.
* COUNT_TIME
  * TIME 및 COUNT 와 유사하지만 두 조건 중 하나라도 참이면 커밋이 수행된다.
* MANUAL
  * 메시지 수신자는 Acknowledgment.acknowledge()를 해야 한다. 그 이후에는 BATCH 와 동일한 시맨틱이 적용된다.
  * 이 옵션을 사용할 경우에는 AcknowledgingMessageListener 또는 BatchAcknowledgingMessageListener 를 리스너로 사용해야 한다.
* MANUAL_IMMEDIATE
  * 리스너가 Acknowledgment.acknowledge() 메서드를 호출하면 즉시 오프셋을 커밋한다.
  * 이 옵션을 사용할 경우에는 AcknowledgingMessageListener 또는 BatchAcknowledgingMessageListener 를 리스너로 사용해야 한다.

```java
public enum AckMode {
		/**
		 * Commit the offset when the listener returns after processing the record.
		 */
		RECORD,

		/**
		 * Commit the offset when all the records returned by the poll() have been processed.
		 */
		BATCH,

		/**
		 * Commit the offset when all the records returned by the poll() have been processed, as long as the ackTime since the last commit has been exceeded.
		 */
		TIME,

		/**
		 * Commit the offset when all the records returned by the poll() have been processed, as long as ackCount records have been received since the last commit.
		 */
		COUNT,

		/**
		 * Similar to TIME and COUNT, but the commit is performed if either condition is true.
		 */
		COUNT_TIME,

		/**
		 * The message listener is responsible to acknowledge() the Acknowledgment. After that, the same semantics as BATCH are applied.
		 */
		MANUAL,

		/**
		 * Commit the offset immediately when the Acknowledgment.acknowledge() method is called by the listener.
		 */
		MANUAL_IMMEDIATE,

	}
```
