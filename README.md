# Seoul_WIFI_Search

서울의 공공 와이파이 위치를 카카오톡 API 를 통해 마킹해주는 애플리케이션

서울 와이파이 검색 api 업데이트 버전입니다.

## 구동 화면

![구동화면](https://jee00609.github.io/assets/images/2020-07-26-success.jpg)

## 문제점


1. 2020-07-01 : 검색한 지역의 와이파이 개수가 1000개 이상일 때 요청 불가 메세지 출력 (해결)

``` 
<![CDATA[ 데이터요청은 한번에 최대 1000건을 넘을 수 없습니다. 요청종료위치에서 요청시작위치를 뺀 값이 1000을 넘지 않도록 수정하세요. ]]>
```

2. 2020-07-26 : 
   * 현재 위치(구) 뿐만 아니라 Default 값으로 정해진 위치(중구)의 공공와이파이 위치까지 잡히는 문제가 존재
   * 쓰레드를 갱신할 때 한번 삭제해 주는 단계가 필요할듯 싶다. 


## 현재 단계

[공공데이터 자료들을 카카오톡 맵 마커로 찍기](https://jee00609.github.io//android/KakaoMap-With-SeoulAPI/)
