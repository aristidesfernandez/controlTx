package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.InterfaceBoard} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.InterfaceBoardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interface-boards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ipAddress;

    private StringFilter hash;

    private StringFilter mac;

    private LongFilter dataSheetInterfaceId;

    private Boolean distinct;

    public InterfaceBoardCriteria() {}

    public InterfaceBoardCriteria(InterfaceBoardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ipAddress = other.ipAddress == null ? null : other.ipAddress.copy();
        this.hash = other.hash == null ? null : other.hash.copy();
        this.mac = other.mac == null ? null : other.mac.copy();
        this.dataSheetInterfaceId = other.dataSheetInterfaceId == null ? null : other.dataSheetInterfaceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InterfaceBoardCriteria copy() {
        return new InterfaceBoardCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIpAddress() {
        return ipAddress;
    }

    public StringFilter ipAddress() {
        if (ipAddress == null) {
            ipAddress = new StringFilter();
        }
        return ipAddress;
    }

    public void setIpAddress(StringFilter ipAddress) {
        this.ipAddress = ipAddress;
    }

    public StringFilter getHash() {
        return hash;
    }

    public StringFilter hash() {
        if (hash == null) {
            hash = new StringFilter();
        }
        return hash;
    }

    public void setHash(StringFilter hash) {
        this.hash = hash;
    }

    public StringFilter getMac() {
        return mac;
    }

    public StringFilter mac() {
        if (mac == null) {
            mac = new StringFilter();
        }
        return mac;
    }

    public void setMac(StringFilter mac) {
        this.mac = mac;
    }

    public LongFilter getDataSheetInterfaceId() {
        return dataSheetInterfaceId;
    }

    public LongFilter dataSheetInterfaceId() {
        if (dataSheetInterfaceId == null) {
            dataSheetInterfaceId = new LongFilter();
        }
        return dataSheetInterfaceId;
    }

    public void setDataSheetInterfaceId(LongFilter dataSheetInterfaceId) {
        this.dataSheetInterfaceId = dataSheetInterfaceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterfaceBoardCriteria that = (InterfaceBoardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(hash, that.hash) &&
            Objects.equals(mac, that.mac) &&
            Objects.equals(dataSheetInterfaceId, that.dataSheetInterfaceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ipAddress, hash, mac, dataSheetInterfaceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "") +
            (hash != null ? "hash=" + hash + ", " : "") +
            (mac != null ? "mac=" + mac + ", " : "") +
            (dataSheetInterfaceId != null ? "dataSheetInterfaceId=" + dataSheetInterfaceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
