package com.ScholarShare.dao;

import com.ScholarShare.entity.ModerationLog;
import java.util.List;

public interface ModerationLogDao {
    boolean addLog(ModerationLog log);
    List<ModerationLog> getAllLogs();
}
