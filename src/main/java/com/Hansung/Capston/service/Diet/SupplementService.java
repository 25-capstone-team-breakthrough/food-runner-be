package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.repository.PreferredSupplementRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementService {
  private final SupplementDataRepository supplementDataRepository;
  private final PreferredSupplementRepository preferredSupplementRepository;

  @Autowired
  public SupplementService(SupplementDataRepository supplementDataRepository,
      PreferredSupplementRepository preferredSupplementRepository) {
    this.supplementDataRepository = supplementDataRepository;
    this.preferredSupplementRepository = preferredSupplementRepository;
  }


}
