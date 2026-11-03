package com.flexisa.importer;

import java.util.List;

public record ImportResult(List<ImportedOrder> accepted, List<RejectedRecord> rejected) {
    public ImportResult {
        accepted = List.copyOf(accepted);
        rejected = List.copyOf(rejected);
    }
}
