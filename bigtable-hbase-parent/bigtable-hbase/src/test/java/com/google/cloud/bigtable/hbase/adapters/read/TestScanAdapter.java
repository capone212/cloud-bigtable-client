/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.hbase.adapters.read;

import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.RowFilter;
import com.google.bigtable.v2.RowFilter.Chain;
import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.RowSet;
import com.google.cloud.bigtable.hbase.BigtableExtendedScan;
import com.google.cloud.bigtable.hbase.adapters.filters.FilterAdapter;
import com.google.cloud.bigtable.hbase.adapters.read.ReadHooks;
import com.google.cloud.bigtable.hbase.adapters.read.ScanAdapter;
import com.google.cloud.bigtable.util.ByteStringer;
import com.google.common.base.Function;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Lightweight tests for the ScanAdapter. Many of the methods, such as filter building are
 * already tested in {@link TestGetAdapter}.
 */
@RunWith(JUnit4.class)
public class TestScanAdapter {

  private final static ScanAdapter scanAdapter = new ScanAdapter(FilterAdapter.buildAdapter());
  private final static ReadHooks throwingReadHooks = new ReadHooks() {
    @Override
    public void composePreSendHook(Function<ReadRowsRequest, ReadRowsRequest> newHook) {
      throw new IllegalStateException("Read hooks not supported in TestScanAdapter.");
    }

    @Override
    public ReadRowsRequest applyPreSendHook(ReadRowsRequest readRowsRequest) {
      throw new IllegalStateException("Read hooks not supported in TestScanAdapter.");
    }
  };

  private static RowRange toRange(byte[] start, byte[] stop) {
    return RowRange.newBuilder().setStartKeyClosed(ByteStringer.wrap(start))
        .setEndKeyOpen(ByteStringer.wrap(stop)).build();
  }

  private static RowSet toRowSet(RowRange range) {
    return RowSet.newBuilder().addRowRanges(range).build();
  }

  private static byte[] calculatePrefixEnd(byte[] prefix) {
    byte[] prefixEnd = new byte[prefix.length];
    System.arraycopy(prefix, 0, prefixEnd, 0, prefixEnd.length);
    prefixEnd[prefixEnd.length - 1]++;
    return prefixEnd;
  }

  @Test
  public void testStartAndEndKeysAreSet() {
    byte[] startKey = Bytes.toBytes("startKey");
    byte[] stopKey = Bytes.toBytes("stopKey");
    Scan scan = new Scan(startKey, stopKey);
    ReadRowsRequest.Builder request = scanAdapter.adapt(scan, throwingReadHooks);
    Assert.assertEquals(toRowSet(toRange(startKey, stopKey)), request.getRows());
  }

  @Test
  public void testPrefix() {
    byte[] prefix = Bytes.toBytes("prefix");
    byte[] prefixEnd = calculatePrefixEnd(prefix);
    Scan scan = new Scan();
    scan.setRowPrefixFilter(prefix);
    ReadRowsRequest.Builder request = scanAdapter.adapt(scan, throwingReadHooks);
    Assert.assertEquals(toRowSet(toRange(prefix, prefixEnd)), request.getRows());
  }

  @Test
  public void maxVersionsIsSet() {
    Scan scan = new Scan();
    scan.setMaxVersions(10);
    ReadRowsRequest.Builder rowRequestBuilder = scanAdapter.adapt(scan, throwingReadHooks);
    Assert.assertEquals(
        Chain.newBuilder()
            .addFilters(RowFilter.newBuilder()
                .setFamilyNameRegexFilter(".*"))
            .addFilters(RowFilter.newBuilder()
                .setCellsPerColumnLimitFilter(10))
            .build(),
        rowRequestBuilder.getFilter().getChain());
  }

  @Test
  public void testExtendedScan(){
    byte[] row1 = Bytes.toBytes("row1");
    byte[] row2 = Bytes.toBytes("row2");

    byte[] startRow = Bytes.toBytes("startKey");
    byte[] stopRow = Bytes.toBytes("stopKey");

    byte[] prefix = Bytes.toBytes("prefix");
    byte[] prefixEnd = calculatePrefixEnd(prefix);

    BigtableExtendedScan scan = new BigtableExtendedScan();
    scan.addRowKey(row1);
    scan.addRowKey(row2);
    scan.addRange(startRow, stopRow);
    scan.addRangeWithPrefix(prefix);

    RowSet expected = RowSet.newBuilder()
        .addRowKeys(ByteStringer.wrap(row1))
        .addRowKeys(ByteStringer.wrap(row2))
        .addRowRanges(toRange(startRow, stopRow))
        .addRowRanges(toRange(prefix, prefixEnd))
        .build();

    Assert.assertEquals(expected, scanAdapter.adapt(scan, throwingReadHooks).getRows());
  }
}
