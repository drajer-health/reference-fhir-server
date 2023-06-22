package com.interopx.fhir.validator.configuration;



import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class JSONObjectUserType. */
public class JSONObjectUserType implements UserType {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectUserType.class);
  /**
   * Return the SQL type codes for the columns mapped by this type. The codes are defined on
   * <tt>java.sql.Types</tt>.
   *
   * @return int[] the typecodes
   * @see java.sql.Types
   */
  public int[] sqlTypes() {
    LOGGER.info("Entered - sqlTypes Method in JSONObjectUserType ");
    return new int[] {Types.JAVA_OBJECT};
  }

  /**
   * The class returned by <tt>nullSafeGet()</tt>.
   *
   * @return Class
   */
  public Class<String> returnedClass() {
    LOGGER.info("Entered - returnedClass Method in JSONObjectUserType ");

    return String.class;
  }

  /**
   * Compare two instances of the class mapped by this type for persistence "equality". Equality of
   * the persistent state.
   *
   * @param x the x
   * @param y the y
   * @return boolean
   */
  public boolean equals(Object x, Object y) {
    LOGGER.info("Entered - equals Method in JSONObjectUserType ");

    if (x == null) {

      return y == null;
    }
    LOGGER.info("Exit - equals Method in JSONObjectUserType ");

    return x.equals(y);
  }

  /**
   * Get a hashcode for the instance, consistent with persistence "equality".
   *
   * @param x the x
   * @return the int
   */
  public int hashCode(Object x) {
    LOGGER.info("Entered - hashCode Method in JSONObjectUserType ");

    return x.hashCode();
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset. Implementors should handle
   * possibility of null values.
   *
   * @param rs a JDBC result set
   * @param names the column names
   * @param session the session
   * @param owner the containing entity @return Object
   * @return the object
   * @throws HibernateException the hibernate exception
   * @throws SQLException the SQL exception
   * @throws org.hibernate.HibernateException the org.hibernate. hibernate exception
   */
  public Object nullSafeGet(
      ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    LOGGER.info("Entered - nullSafeGet Method in JSONObjectUserType ");

    if (rs.getString(names[0]) == null) {
      return null;
    }
    return rs.getString(names[0]);
  }

  /**
   * Write an instance of the mapped class to a prepared statement. Implementors should handle
   * possibility of null values. A multi-column type should be written to parameters starting from
   * <tt>index</tt>.
   *
   * @param st a JDBC prepared statement
   * @param value the object to write
   * @param index statement parameter index
   * @param session the session
   * @throws HibernateException the hibernate exception
   * @throws SQLException the SQL exception
   * @throws org.hibernate.HibernateException the org.hibernate. hibernate exception
   */
  public void nullSafeSet(
      PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    LOGGER.info("Entered - nullSafeSet Method in JSONObjectUserType ");
    if (value == null) {
      st.setNull(index, Types.OTHER);
      return;
    }

    st.setObject(index, value, Types.OTHER);
  }

  /**
   * Return a deep copy of the persistent state, stopping at entities and at collections. It is not
   * necessary to copy immutable objects, or null values, in which case it is safe to simply return
   * the argument.
   *
   * @param value the object to be cloned, which may be null
   * @return Object a copy
   * @throws HibernateException the hibernate exception
   */
  public Object deepCopy(Object value) {
    LOGGER.info("Entered - deepCopy Method in JSONObjectUserType ");

    return value;
  }

  /**
   * Are objects of this type mutable?.
   *
   * @return boolean
   */
  public boolean isMutable() {
    LOGGER.info("Entered - isMutable Method in JSONObjectUserType ");

    return true;
  }

  /**
   * Transform the object into its cacheable representation. At the very least this method should
   * perform a deep copy if the type is mutable. That may not be enough for some implementations,
   * however; for example, associations must be cached as identifier values. (optional operation)
   *
   * @param value the object to be cached
   * @return a cachable representation of the object
   * @throws HibernateException the hibernate exception
   * @throws org.hibernate.HibernateException the org.hibernate. hibernate exception
   */
  public Serializable disassemble(Object value) {
    LOGGER.info("Entered - disassemble Method in JSONObjectUserType ");

    return (String) this.deepCopy(value);
  }

  /**
   * Reconstruct an object from the cacheable representation. At the very least this method should
   * perform a deep copy if the type is mutable. (optional operation)
   *
   * @param cached the object to be cached
   * @param owner the owner of the cached object
   * @return a reconstructed object from the cachable representation
   * @throws HibernateException the hibernate exception
   * @throws org.hibernate.HibernateException the org.hibernate. hibernate exception
   */
  public Object assemble(Serializable cached, Object owner) {
    LOGGER.info("Entered - assemble Method in JSONObjectUserType ");

    return this.deepCopy(cached);
  }

  /**
   * During merge, replace the existing (target) value in the entity we are merging to with a new
   * (original) value from the detached entity we are merging. For immutable objects, or null
   * values, it is safe to simply return the first parameter. For mutable objects, it is safe to
   * return a copy of the first parameter. For objects with component values, it might make sense to
   * recursively replace component values.
   *
   * @param original the value from the detached entity being merged
   * @param target the value in the managed entity
   * @param owner the owner
   * @return the value to be merged
   * @throws HibernateException the hibernate exception
   */
  public Object replace(Object original, Object target, Object owner) {
    LOGGER.info("Entered - replace Method in JSONObjectUserType ");

    return original;
  }
}
