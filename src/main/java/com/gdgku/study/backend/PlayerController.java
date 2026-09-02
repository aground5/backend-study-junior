package com.gdgku.study.backend;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    // 메모리(RAM) 저장소 역할
    private final List<Player> playerList = new ArrayList<>();
    private long nextId = 1L;

    // 초기 데이터 등록 (LG Twins 대표 선수 예시)
    public PlayerController() {
        playerList.add(new Player(nextId++, 10, "오지환", "LG Twins", "IF", "1990-03-12", 186, 85));
        playerList.add(new Player(nextId++, 23, "홍창기", "LG Twins", "OF", "1993-11-21", 189, 94));
        playerList.add(new Player(nextId++, 1, "임찬규", "LG Twins", "P", "1992-11-20", 186, 92));
    }

    // =========================================================================
    // 0. Player 내부 데이터 클래스 (DTO/Entity)
    // =========================================================================
    public static class Player {
        private Long id;           // 고유 식별자 (PK)
        private Integer backNumber; // 등번호
        private String name;       // 선수명
        private String team;       // 팀명
        private String position;   // 포지션 (투수, 포수, 내야수, 외야수)
        private String birthDate;  // 생년월일 (YYYY-MM-DD)
        private Integer height;    // 키 (cm)
        private Integer weight;    // 몸무게 (kg)

        // 1) 기본 생성자 (Jackson 역직렬화 필수)
        public Player() {}

        // 2) 전체 필드 생성자
        public Player(Long id, Integer backNumber, String name, String team, String position, String birthDate, Integer height, Integer weight) {
            this.id = id;
            this.backNumber = backNumber;
            this.name = name;
            this.team = team;
            this.position = position;
            this.birthDate = birthDate;
            this.height = height;
            this.weight = weight;
        }

        // 3) Getter & Setter
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Integer getBackNumber() { return backNumber; }
        public void setBackNumber(Integer backNumber) { this.backNumber = backNumber; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getTeam() { return team; }
        public void setTeam(String team) { this.team = team; }

        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }

        public String getBirthDate() { return birthDate; }
        public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

        public Integer getHeight() { return height; }
        public void setHeight(Integer height) { this.height = height; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }
    }

    // =========================================================================
    // 1. Create (선수 생성)
    // POST http://localhost:8080/players
    // =========================================================================
    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        player.setId(nextId++);
        
        // 팀명이 안 들어왔을 경우 기본값 지정
        if (player.getTeam() == null || player.getTeam().isBlank()) {
            player.setTeam("LG Twins");
        }
        
        playerList.add(player);
        return player;
    }

    // =========================================================================
    // 2. Read (선수 조회)
    // 2-1. 전체 조회 or 이름 검색: GET http://localhost:8080/players
    //      (예: GET http://localhost:8080/players?name=오지환)
    // =========================================================================
    @GetMapping
    public List<Player> getPlayers(@RequestParam(required = false) String name) {
        // name 쿼리 파라미터가 없으면 전체 목록 반환
        if (name == null || name.isBlank()) {
            return playerList;
        }

        // name 파라미터가 있으면 해당 이름의 선수만 필터링하여 반환
        List<Player> filteredList = new ArrayList<>();
        for (Player p : playerList) {
            if (p.getName().equals(name)) {
                filteredList.add(p);
            }
        }
        return filteredList;
    }

    // 2-2. ID로 단일 선수 상세 조회: GET http://localhost:8080/players/1
    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        for (Player p : playerList) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null; // 실무에서는 예외(Exception) 처리
    }

    // =========================================================================
    // 3. Update (선수 정보 수정)
    // PUT http://localhost:8080/players/1
    // =========================================================================
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        for (Player p : playerList) {
            if (p.getId().equals(id)) {
                // 등번호, 팀, 포지션, 키, 몸무게 등 정보 교체
                if (updatedPlayer.getBackNumber() != null) p.setBackNumber(updatedPlayer.getBackNumber());
                if (updatedPlayer.getTeam() != null) p.setTeam(updatedPlayer.getTeam());
                if (updatedPlayer.getPosition() != null) p.setPosition(updatedPlayer.getPosition());
                if (updatedPlayer.getHeight() != null) p.setHeight(updatedPlayer.getHeight());
                if (updatedPlayer.getWeight() != null) p.setWeight(updatedPlayer.getWeight());
                
                return p;
            }
        }
        return null;
    }

    // =========================================================================
    // 4. Delete (선수 삭제)
    // DELETE http://localhost:8080/players/1
    // =========================================================================
    @DeleteMapping("/{id}")
    public String deletePlayer(@PathVariable Long id) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId().equals(id)) {
                String removedName = playerList.get(i).getName();
                playerList.remove(i);
                return removedName + " 선수가 명단에서 삭제되었습니다.";
            }
        }
        return "해당 ID의 선수를 찾을 수 없습니다.";
    }
}