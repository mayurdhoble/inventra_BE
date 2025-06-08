package com.business.inventra.dto.response;

import com.business.inventra.dto.BranchDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BranchResponse {
    private BranchDTO branch;

 

    public BranchResponse(BranchDTO branch) {
        this.branch = branch;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }
} 