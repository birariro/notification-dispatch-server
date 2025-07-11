# 알림 발송 서버
알림을 처리 가능한 프로바이더들에게 알림을 발송한다.

## 시스템 구성
- api: 사용자의 알림 발송 요청
- work queue : 알림 발송 작업 대기열
- recovery queue : 장애 발생 시 실패한 작업을 재처리하기 위한 대기열
- provider : 알림 발송 서버와 연동되는 메시지 전송 모듈
- scheduler: 예약 시간이 도래한 알림을 주기적으로 작업 대기열에 등록

## api

### 알림 발송

- method : post
- url : /notification
- body
  - memberId: 발신자 ID
  - recipientId: 수신자 ID
  - title: 메시지 타이틀
  - contents: 메시지 내용
  - channel: 알림 채널 종류(SMS, PUSH, EMAIL)
  - scheduledDate(optional): 발송일 예약(yyyyMMddhhmm)

사용 예

```shell
curl -X POST http://127.0.0.1:8080/notification \
-H "Content-Type: application/json" \
-d '{
  "memberId": "f3ea3f5f-7185-4f30-8a78-cb964778d516",
  "recipientId": "f3ea3f5f-7185-4f30-8a78-e3dbefe186a4",
  "title": "메시지 타이틀",
  "contents": "메시지 내용",
  "channel": "SMS"
}'
```


## 프로젝트 설명

### 디렉토리 구조

```
├── adapter //외부 구성 요소들과 통신을 담당
│ ├── controller //클라이언트
│ ├── provider //메시지 발송 서버
│ └── queue //메시지 큐
│
├── application //발송 등록, 조회 등 해당 프로젝트의 기능
├── mock // mock provider
│
└── infra //해당 프로젝트 동작에 필요한 설정
```


### Recovery Queue 정책 개선
- 문제 발생: 발송 장애 시, 실패한 요청이 제한 없이 Recovery Queue에 쌓이고, 프로바이더가 장애 상태인 경우 즉시 재시도되면서 서버에 과도한 부하가 발생. 이는 "복구 큐 → 실패 → 복구 큐"의 무한 루프를 유발.
- 1차 조치: 고정 백오프 적용: 즉시 재시도 대신 일정한 시간 간격으로 재시도하는 고정 백오프 방식을 적용하여, 반복적인 부하 감소.
- 문제 재발: 임계값 초과: 하지만 이 방식은 네트워크 임계값을 초과해 일시적으로 실패한 경우에도 동일 간격으로 재시도가 발생하여, 지속적인 임계값 초과 현상이 반복되는 문제 발생.
- 최종 해결: 지수 백오프 도입: 실패 시마다 재시도 간격을 점점 늘리는 지수 백오프 방식으로 전환하여, 임계값 초과 상황에서도 점진적인 부하 분산이 가능하도록 개선.
- 예외 처리: 최대 재시도 간격 설정: 다만, 재시도 간격이 과도하게 길어져 정상 복구 이후에도 발송 지연이 발생하지 않도록 최대 재시도 시간 상한선을 설정해 사용자 경험 저하를 방지.
