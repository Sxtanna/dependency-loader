package com.sxtanna.base;

import com.google.common.base.Objects;
import com.sxtanna.util.Urls;
import org.jetbrains.annotations.NotNull;

public final class DOptions {

	@NotNull
	private String  customRepository;
	private boolean alwaysUpdate;


	DOptions(@NotNull String customRepository, boolean alwaysUpdate) {
		this.customRepository = customRepository;
		this.alwaysUpdate = alwaysUpdate;
	}


	@NotNull
	public String getCustomRepository() {
		return customRepository;
	}

	public void setCustomRepository(@NotNull String customRepository) {
		this.customRepository = Urls.fixUrl(customRepository);
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
