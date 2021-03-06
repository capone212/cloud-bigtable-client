// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface MutateRowRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.MutateRowRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The unique name of the table to which the mutation should be applied.
   * Values are of the form
   * projects/&amp;lt;project&amp;gt;/instances/&amp;lt;instance&amp;gt;/tables/&amp;lt;table&amp;gt;
   * </pre>
   *
   * <code>optional string table_name = 1;</code>
   */
  java.lang.String getTableName();
  /**
   * <pre>
   * The unique name of the table to which the mutation should be applied.
   * Values are of the form
   * projects/&amp;lt;project&amp;gt;/instances/&amp;lt;instance&amp;gt;/tables/&amp;lt;table&amp;gt;
   * </pre>
   *
   * <code>optional string table_name = 1;</code>
   */
  com.google.protobuf.ByteString
      getTableNameBytes();

  /**
   * <pre>
   * The key of the row to which the mutation should be applied.
   * </pre>
   *
   * <code>optional bytes row_key = 2;</code>
   */
  com.google.protobuf.ByteString getRowKey();

  /**
   * <pre>
   * Changes to be atomically applied to the specified row. Entries are applied
   * in order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3;</code>
   */
  java.util.List<com.google.bigtable.v2.Mutation> 
      getMutationsList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row. Entries are applied
   * in order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3;</code>
   */
  com.google.bigtable.v2.Mutation getMutations(int index);
  /**
   * <pre>
   * Changes to be atomically applied to the specified row. Entries are applied
   * in order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3;</code>
   */
  int getMutationsCount();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row. Entries are applied
   * in order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutationOrBuilder> 
      getMutationsOrBuilderList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row. Entries are applied
   * in order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3;</code>
   */
  com.google.bigtable.v2.MutationOrBuilder getMutationsOrBuilder(
      int index);
}
