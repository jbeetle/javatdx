package org.zoomdev.stock.txd.reader;

import org.zoomdev.stock.txd.BlockStock;
import org.zoomdev.stock.txd.TypedInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockReader {

    public static List<BlockStock> read(byte[] content) throws IOException {
        TypedInputStream inputStream = new TypedInputStream(content);
        inputStream.skip(384);
        int blockCount = inputStream.readShort();
        List<BlockStock> result = new ArrayList<BlockStock>(blockCount);
        for(int i=0; i < blockCount; ++i){
            String blockName = inputStream.readGbkString(9);
            int stockCount = inputStream.readShort();
            assert (stockCount<10000);
            int blockType = inputStream.readShort();
            int block_stock_begin = inputStream.getPos();
            BlockStock blockStock = new BlockStock();
            result.add(blockStock);
            blockStock.setBlockName(blockName);
            blockStock.setBlockType(blockType);

            List<String> codes = new ArrayList<String>( stockCount );
            blockStock.setCodes(codes);
            for(int code_index=0; code_index < stockCount; ++code_index){
                String code = inputStream.readUtf8String(7);
                codes.add(code);
            }
            inputStream.setPos(block_stock_begin + 2800);

        }

        return result;

    }
}
