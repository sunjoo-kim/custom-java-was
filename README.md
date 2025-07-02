# custom-java-was
A lightweight Java web application server built from scratch, without using servlet containers like Tomcat.
=======

## 📝 프로젝트 개요
**company WAS Project**는 Java와 Maven 기반의 웹 애플리케이션 서버(WAS)를 직접 구현한 프로젝트입니다.  
HTTP 요청 처리, 가상 호스트 설정, 요청 필터링 등 서버의 핵심 기능을 이해하고 직접 구현해보는 데 목적이 있습니다.

---

## 🛠 기술 스택
- **Language**: Java17
- **Build Tool**: Maven
- **테스트 프레임워크**: JUnit4
- **로깅 프레임워크**: Logback

---

## 🔧 주요 구성 요소

### 1. `Config.java`
- **역할**: 서버 설정 로드 및 가상 호스트 매핑
- **경로**: `src/main/java/com/company/was/config/Config.java`
- **주요 메서드**:
   - `getVirtualHost(String host)`
   - `getAllVirtualHosts()`

---

### 2. `FilterChain.java`
- **역할**: 요청 필터들을 체이닝 방식으로 실행하여 보안 필터 구성
- **경로**: `src/main/java/com/company/was/core/filter/FilterChain.java`
- **주요 메서드**:
   - `addFilter(RequestFilter filter)`: 필터를 체인에 추가
   - `doFilter(DefaultHttpRequest request, HttpResponse response)`: 필터를 순차적으로 실행, 하나라도 실패하면 중단
   - `getFilters()`: 등록된 모든 필터 리스트 반환
- **특징**: 필터 체이닝 구조를 통해 보안 정책을 유연하게 확장 가능

---

### 3. `GetHandler.java`
- **역할**: HTTP GET 요청을 처리하는 핵심 핸들러 클래스
- **경로**: `src/main/java/com/company/was/core/handler/GetHandler.java`
- **주요 기능**:
   - **서블릿 호출**: 요청 URL이 클래스 경로를 포함하는 경우 해당 서블릿 클래스를 동적으로 로딩하고, 인스턴스를 생성하여 `Servlet` 인터페이스의 `service` 메서드를 호출해 응답을 생성
   - **정적 리소스 처리**: 요청 URL이 파일 확장자를 포함하는 경우, 클래스패스 내 리소스를 읽어 HTTP 응답으로 반환. 리소스가 없으면 404 응답 반환
   - **예외 처리 및 로깅**: 클래스 로딩 실패, 인스턴스 생성 오류 등 예외 상황을 로깅 처리하여 디버깅 용이
- **특징**:
   - 리플렉션을 활용한 유연한 서블릿 핸들링 구조
   - 파일 리소스 제공 기능 내장으로 간단한 정적 콘텐츠 서빙 가능

---

### 4. `Dispatcher.java`
- **역할**: HTTP 요청을 받아 필터링 후 적절한 핸들러로 전달하는 중앙 디스패처 클래스
- **경로**: `src/main/java/com/company/was/core/Dispatcher.java`
- **주요 기능**:
   - **필터 체인 적용**: `FilterChain`에 등록된 여러 필터(`DirectoryTraversalFilter`, `ExeFileFilter`)를 순차적으로 적용하여 보안 규칙을 검사
   - **요청 처리 분기**: HTTP 메서드에 따라 처리 핸들러를 분기, 현재는 GET 요청만 `GetHandler`로 처리하며, 그 외는 404 응답 반환
   - **보안 위반 시 403 반환**: 필터 통과 실패 시 HTTP 403 Forbidden 응답 반환
   - **로깅**: 필터 체인 내 필터 목록과 처리 과정을 로깅하여 디버깅 및 추적에 활용
- **특징**:
   - 필터 체인 패턴을 이용해 확장성과 유지보수성을 높임
   - 보안 관련 필터들을 한 곳에서 관리하여 중앙 집중식 보안 관리 구현

---

### 5. `request pakage`
- **역할**: sample HTTP 요청을 생성하고 테스트하는 패키지
- **경로**: `request`
- **주요 기능**: 유스케이스별 샘플 요청 모음

---

### 6. `logback.xml`
- **역할**: logging 설정 파일
- **경로**: `logback.xml`
- **주요 기능**: 로그 파일을 하루 단위로 분리

---

## 🚀 실행 방법

1. Maven 빌드:
   ```bash
   mvn clean package
   java -jar target/companyAbcTest-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
