# 🛠 점검중 플러그인 (Maintenance Mode Plugin)

마인크래프트 서버용 점검 모드 플러그인입니다.  
`config.yml`을 통해 **점검 자동 적용**, **허용 플레이어 설정**, **차단 메시지** 등을 손쉽게 관리할 수 있습니다.

---

## ⚡ 주요 기능

- 특정 날짜와 시간에 **자동 점검 모드 적용**
- 점검 모드 중 **일반 플레이어 접속 제한**
- 점검 중 허용할 플레이어 닉네임 설정 가능
- **차단 메시지 커스터마이징** 지원
- 서버 시간대(`timezone`) 설정 가능

---

## 🚀 설치 방법

1. 플러그인 `.jar` 파일을 `plugins` 폴더에 넣습니다.
2. 서버를 재시작하거나 `/reload` 명령어를 실행합니다.
3. `plugins/점검중/config.yml` 파일이 생성됩니다.

---

## 📝 config.yml 예시

```yaml
# 점검 모드 자동 적용 날짜 및 시간 (UTC-9 기준)
maintenance:
  start-date: "2025-08-20"  # 점검 시작 날짜
  end-date: "2025-08-25"    # 점검 종료 날짜
  start-time: "01:00"       # 점검 시작 시간
  end-time: "09:00"         # 점검 종료 시간

# 점검 모드 시 허용 닉네임
allowed-names:
  - MyName

# 접속 차단 메시지
kick-message: |
  &6&l서버 점검 중!
  &e현재 서버는 점검 중입니다.
  &e점검 시간: &a%startDate% &e~ &a%endDate% (UTC-9)
  &b잠시 후 다시 접속해주세요!

# 점검 시간대
timezone: "Asia/Seoul"
```

### 🔧 설정 설명

| 항목 | 설명 |
|------|------|
| `maintenance.start-date` / `end-date` | 점검 시작 및 종료 날짜 |
| `maintenance.start-time` / `end-time` | 점검 시작 및 종료 시간 |
| `allowed-names` | 점검 중 접속 허용 플레이어 닉네임 |
| `kick-message` | 점검 중 접속 차단 시 보여줄 메시지 (`%startDate%`, `%endDate%` 치환 가능) |
| `timezone` | 서버 시간대 설정 (기본: `"Asia/Seoul"`) |

---

### 🛠 명령어

| 명령어 | 설명 | 권한 |
|--------|------|------|
| `/점검중` | 점검 모드 ON/OFF 전환 | OP 또는 Admin |

---

### 🛡 권한

- 기본적으로 **서버 OP 권한** 필요
- 별도 권한 플러그인과 연동 가능

---

### 📌 업데이트 & 지원

- GitHub: [https://github.com/wltp0321/wltpJoinBlocker](https://github.com/wltp0321/wltpJoinBlocker)
- 문의: [문의 이메일 또는 디스코드]
