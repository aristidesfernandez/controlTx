package co.com.ies.smol.service.dto.core;

import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record RequestStatusRecord(String msg, Integer code) implements Serializable {}
