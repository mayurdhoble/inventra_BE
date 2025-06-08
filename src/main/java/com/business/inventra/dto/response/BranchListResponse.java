package com.business.inventra.dto.response;

import com.business.inventra.dto.BranchDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BranchListResponse {
    private List<BranchDTO> branches;

    public BranchListResponse(List<BranchDTO> branches) {
        this.branches = branches;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
} 