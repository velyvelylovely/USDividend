# 💵Dividend Service
미국 주식 배당금 정보를 제공하는 API  
[배당금 정보 제공 프로젝트 정보](https://dev-rara.notion.site/Dividend-Service-ffa52b3a23344da1b4c4df4a0cbfdfab)

## 🛠️사용 기술 스택
* Spring Boot
* H2-Database
* Spring JPA
* Spring Security + JWT
* Redis
* Jsoup
* LogBack
* Swagger

## 💡구현 기능
* ID와 비밀번호를 이용한 회원가입 및 로그인
* 회사명을 이용한 해당 회사의 정보와 배당금 정보 조회
* 서비스에서 관리중인 모든 회사 목록 조회
* 자동완성 기능으로 입력받은 키워드에 해당하는 회사 목록 조회
* 새로운 회사 정보 저장
* 입력받은 ticker를 이용한 회사 정보 삭제
* 배당금 정보를 가져오기 위한 웹 스크래핑
* 일정 주기로 배당금 정보 업데이트
* 데이터 캐싱, 캐시 삭제
* 예외 혹은 에러 발생시 로그 기록
* API Document를 통한 Rest API 명세 확인

<img width="924" alt="스크린샷 2022-09-17 02 06 06" src="https://user-images.githubusercontent.com/65327103/190693349-84cd0c29-77e8-4298-a711-067be7849d19.png">
