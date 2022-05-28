# urlshortener

## 소개
단축 URL 서비스를 제공하는 웹 어플리케이션입니다.

## 요구사항
- url 등록 화면
    - 유저가 original url을 등록한다.
    - 유저가 입력한 url에 대응하는 단축 url 생성한다.
    - 만약 중복된 단축 url이 있다면 다시 단축 url을 생성한다.
    - 단축 url이 생성되면 유저에게 성공적으로 단축 url이 생성되었음을 알린다. 
- 생성된 단축 url로 요청시 원래 url로 redirect
    - 일치하는 단축 url이 없으면 잘못된 입력임을 알린다.   
    - 단축 url로 요청이 오면 요청 횟수를 저장한다.
- 등록한 단축 url 조회
    - 각 단축 url마다 요청된 횟수도 확인가능하다.

## ERD
![er_diagram](https://user-images.githubusercontent.com/86475543/160814510-b40cad0c-09b1-4a8f-8a10-696f6af2b41a.png)

## 이슈 정리 링크
https://trusting-dryer-226.notion.site/urlshortener-eb36b9ad5adb440bada0ba76c6ee2107
