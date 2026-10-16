package com.flexisa.data;

import java.util.List;

public record LoadResult<T>(List<T> valid, List<String> errors) {
}
