package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.entity.Diet.Supplement.PreferredSupplement;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementLog;
import com.Hansung.Capston.repository.Diet.Supplement.PreferredSupplementRepository;
import com.Hansung.Capston.repository.Diet.Supplement.SupplementDataRepository;
import com.Hansung.Capston.repository.Diet.Supplement.SupplementLogRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementService {
  private final SupplementDataRepository supplementDataRepository;
  private final SupplementLogRepository supplementLogRepository;
  private final PreferredSupplementRepository preferredSupplementRepository;
  private final UserRepository userRepository;

  @Autowired
  public SupplementService(SupplementDataRepository supplementDataRepository,
      SupplementLogRepository supplementLogRepository,
      PreferredSupplementRepository preferredSupplementRepository, UserRepository userRepository) {
    this.supplementDataRepository = supplementDataRepository;
    this.supplementLogRepository = supplementLogRepository;
    this.preferredSupplementRepository = preferredSupplementRepository;
    this.userRepository = userRepository;
  }

  // 영양제 데이터 불러오기
  public List<SupplementData> loadSupplementData() {
    return supplementDataRepository.findAll();
  }
  
  // 영양제 섭취 기록 등록하기
  public void saveSupplementLog(String userId, LocalDateTime dateTime, Long supplementId) {
    SupplementLog supplementLog = new SupplementLog();

    supplementLog.setUser(userRepository.findById(userId).get());
    supplementLog.setDate(dateTime);
    supplementLog.setSupplementData(supplementDataRepository.findById(supplementId).get());

    supplementLogRepository.save(supplementLog);
  }
  
  // 영양제 섭취 기록 불러오기
  public List<SupplementLog> loadSupplementLogs(String userId) {
    List<SupplementLog> supplementLogs = supplementLogRepository.findByUserUserId(userId);
    List<SupplementLog> res = new ArrayList<>();

    for (SupplementLog supplementLog : supplementLogs) {
      supplementLog.setUser(null);
      res.add(supplementLog);
    }

    return res;
  }
  
  // 영양제 섭취 기록 삭제하기
  public void deleteSupplementLog(String userId, Long supplementLogId) {
    supplementLogRepository.deleteById(supplementLogId);
  }

  // 영양제 즐겨찾기 등록하기
  public String savePreferredSupplement(String userId,Long supplementId) {
    PreferredSupplement preferredSupplement;

    if((preferredSupplement = supplementDataRepository.findByUserUserIdAndSupplementDataSupplementId(userId,supplementId)) != null){
      return "실패 : 이미 등록되어 있습니다.";
    } else {
      preferredSupplement = new PreferredSupplement();
      preferredSupplement.setSupplementData(supplementDataRepository.findById(supplementId).get());
      preferredSupplement.setUser(userRepository.findById(userId).get());

      preferredSupplementRepository.save(preferredSupplement);

      return "성공 : 즐겨찾기 등록";
    }
  }
  
  // 영양제 즐겨찾기 불러오기
  public List<PreferredSupplement> getPreferredSupplements(String userId) {
    List<PreferredSupplement> preferredSupplements = preferredSupplementRepository.findByUserUserId(userId);
    List<PreferredSupplement> res = new ArrayList<>();

    for (PreferredSupplement preferredSupplement : preferredSupplements) {
      preferredSupplement.setUser(null);
      res.add(preferredSupplement);
    }

    return res;
  }

  // 영양제 즐겨찾기 삭제하기
  public void deletePreferredSupplement(Long preferredSupplementId) {
    preferredSupplementRepository.deleteById(preferredSupplementId);
  }

}
