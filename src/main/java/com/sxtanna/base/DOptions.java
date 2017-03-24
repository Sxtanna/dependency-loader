package com.sxtanna.base;

import com.google.common.base.Objects;

public final class DOptions {

	private String customRepository;
	private boolean alwaysUpdate;


	DOptions(String customRepository, boolean alwaysUpdate) {
		this.customRepository = customRepository;
		this.alwaysUpdate = alwaysUpdate;
	}


	public String getCustomRepository() {
		return customRepository == null ? "" : customRepository;
	}

	public void setCustomRepository(String customRepository) {
		this.customRepository = customRepository;
	}

	public boolean isAlwaysUpdate() {
		return alwaysUpdate;
	}

	public void setAlwaysUpdate(boolean alwaysUpdate) {
		this.alwaysUpdate = alwaysUpdate;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DOptions)) return false;
		DOptions dOptions = (DOptions) o;
		return isAlwaysUpdate() == dOptions.isAlwaysUpdate() &&
				Objects.equal(getCustomRepository(), dOptions.getCustomRepository());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getCustomRepository(), isAlwaysUpdate());
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("customRepository", getCustomRepository())
				.add("alwaysUpdate", alwaysUpdate)
				.toString();
	}

}
