package com.sxtanna.base;

import com.google.common.base.Objects;
import com.sxtanna.util.Urls;
import org.jetbrains.annotations.NotNull;

/**
 * The Core component of this Util
 * <p>
 * <p>Holds details of the Maven Dependency</p>
 */
@SuppressWarnings("WeakerAccess")
public final class Dependency {

	@NotNull
	private final String name, version, groupId, artifactId;
	@NotNull
	private final DOptions options;

	private Dependency parent = null;

	/**
	 * Create a new Dependency
	 *
	 * @param name       The name of the Dependency, can be anything, but recommended to be something global
	 * @param version    The version of this Maven Artifact
	 * @param groupId    The groupId of this Maven Artifact
	 * @param artifactId The artifactId of this Maven Artifact
	 * @param customRepo The custom repository to load this from
	 */
	public Dependency(@NotNull String name, @NotNull String version, @NotNull String groupId, @NotNull String artifactId, String customRepo, boolean alwaysUpdate) {
		this.name = name;
		this.version = version;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.options = new DOptions(Urls.fixUrl(customRepo), alwaysUpdate);
	}

	/**
	 * @see #Dependency(String, String, String, String, String, boolean)
	 */
	public Dependency(String name, String version, String groupId, String artifactId) {
		this(name, version, groupId, artifactId, "", false);
	}


	/**
	 * The name of this Dependency
	 *
	 * @return The name
	 */
	@NotNull
	public String getName() {
		return name;
	}

	/**
	 * The Maven Version of this Dependency
	 *
	 * @return The version
	 */
	@NotNull
	public String getVersion() {
		return version;
	}

	/**
	 * The Maven Group ID of this Dependency
	 *
	 * @return The groupId
	 */
	@NotNull
	public String getGroupId() {
		return groupId;
	}

	/**
	 * The Maven Artifact ID of this Dependency
	 *
	 * @return The artifactId
	 */
	@NotNull
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * This Dependency's options
	 *
	 * @return The Options
	 */
	@NotNull
	public DOptions getOptions() {
		return options;
	}

	/**
	 * The Parent Dependency, meaning The parent depends on this
	 *
	 * @return The parent
	 */
	public Dependency getParent() {
		return parent;
	}

	/**
	 * Set the parent Dependency
	 *
	 * @param parent The new parent
	 */
	public void setParent(Dependency parent) {
		this.parent = parent;
	}

	/**
	 * Check if this Dependency has a parent
	 *
	 * @return true if it does, false otherwise
	 */
	public boolean hasParent() {
		return getParent() != null;
	}

	/**
	 * Get the depth of this dependency
	 * <p>
	 * <p>Where "this" is this dependency, and "Parent" is it's parent</p>
	 * <p>
	 * <p>this, will return 0</p>
	 * <p>Parent > this, will return 1</p>
	 * <p>Parent > Parent > this, will return 2</p>
	 *
	 * @return The depth of this Dependency's parents
	 */
	public int getParentDepth() {
		int depth = 0;

		Dependency parent = getParent();
		while (parent != null) {
			parent = parent.getParent();
			depth++;
		}

		return depth;
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
