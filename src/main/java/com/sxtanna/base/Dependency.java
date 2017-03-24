package com.sxtanna.base;

import com.google.common.base.Objects;

/**
 * The Core component of this Util
 *
 * <p>Holds details of the Maven Dependency</p>
 */
public final class Dependency {

	private final String name, version, groupId, artifactId;


	/**
	 * Create a new Dependency
	 *
	 * @param name The name of the Dependency, can be anything, but recommended to be something global
	 * @param version The version of this Maven Artifact
	 * @param groupId The groupId of this Maven Artifact
	 * @param artifactId The artifactId of this Maven Artifact
	 */
	public Dependency(String name, String version, String groupId, String artifactId) {
		this.name = name;
		this.version = version;
		this.groupId = groupId;
		this.artifactId = artifactId;
	}


	/**
	 * The name of this Dependency
	 *
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The Maven Version of this Dependency
	 *
	 * @return The version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * The Maven Group ID of this Dependency
	 *
	 * @return The groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * The Maven Artifact ID of this Dependency
	 *
	 * @return The artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}


	/**
	 * The name of this Dependency's Jar file in the Repo
	 *
	 * @return The jar name
	 */
	public String getJarName() {
		return getArtifactId() + "-" + getVersion() + ".jar";
	}

	/**
	 * The name of this Dependency's POM file in the Repo
	 *
	 * @return The pom name
	 */
	public String getPomName() {
		return getArtifactId() + "-" + getVersion() + ".pom";
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Dependency)) return false;
		Dependency that = (Dependency) o;
		return Objects.equal(getVersion(), that.getVersion()) &&
				Objects.equal(getGroupId(), that.getGroupId()) &&
				Objects.equal(getArtifactId(), that.getArtifactId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getVersion(), getGroupId(), getArtifactId());
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("name", name)
				.add("version", version)
				.add("groupId", groupId)
				.add("artifactId", artifactId)
				.toString();
	}

}
