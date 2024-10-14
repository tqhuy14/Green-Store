package com.inventory_management.web.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupFunctionService {
    List<Long> findFunctionsByGroupId(Long groupID);
}
//
//   <div class="form-group" style="padding-bottom: 40px; display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
//                        <button type="submit" class="btn btn-success shadow">Cập nhật</button>
//                        <a class="btn btn-secondary shadow" th:href="@{/roles}">Hủy</a>
//                    </div>