package org.geysermc.connector.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v361.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.geysermc.connector.network.translators.item.BedrockItem;
import org.geysermc.connector.network.translators.item.JavaItem;

import java.io.InputStream;
import java.util.*;

public class Toolbox {

    static {
        InputStream stream = Toolbox.class.getClassLoader().getResourceAsStream("bedrock/cached_palette.json");
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<LinkedHashMap<String, Object>> entries = new ArrayList<>();

        try {
            entries = mapper.readValue(stream, ArrayList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, BedrockItem> m = new HashMap<>();

        Map<String, BedrockItem> bedrockItems = new HashMap<>();
        for (Map<String, Object> e : entries) {
            BedrockItem bedrockItem = new BedrockItem((String) e.get("name"), (int) e.get("id"), (int) e.get("data"));
            m.put(bedrockItem.getIdentifier(), bedrockItem);
        }


        ByteBuf b = Unpooled.buffer();
        VarInts.writeUnsignedInt(b, entries.size());
        for (Map<String, Object> e : entries) {
            BedrockUtils.writeString(b, (String) e.get("name"));
            b.writeShortLE((int) e.get("data"));
            b.writeShortLE((int) e.get("id"));
        }

        CACHED_PALLETE = b;

        InputStream stream2 = Toolbox.class.getClassLoader().getResourceAsStream("bedrock/items.json");
        if (stream2 == null) {
            throw new AssertionError("Items Table not found");
        }

        ObjectMapper mapper2 = new ObjectMapper();
        ArrayList<HashMap> s = new ArrayList<>();
        try {
            s = mapper2.readValue(stream2, ArrayList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StartGamePacket.ItemEntry> l = new ArrayList<>();
        for (HashMap e : s) {
            l.add(new StartGamePacket.ItemEntry((String) e.get("name"), (short) ((int) e.get("id"))));
            if (!bedrockItems.containsKey(e.get("name"))) {
                BedrockItem bedrockItem = new BedrockItem((String) e.get("name"), ((int) e.get("id")), 0);
                bedrockItems.put(bedrockItem.getIdentifier(), bedrockItem);
            }
        }

        ITEMS = l;

        BEDROCK_ITEMS = bedrockItems;

        InputStream javaItemStream = Toolbox.class.getClassLoader().getResourceAsStream("java/java_items.json");
        ObjectMapper javaItemMapper = new ObjectMapper();
        Map<String, HashMap> javaItemList = new HashMap<>();
        try {
            javaItemList = javaItemMapper.readValue(javaItemStream, new TypeReference<Map<String, HashMap>>(){});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Map<String, JavaItem> javaItems = new HashMap<String, JavaItem>();

        for (String str : javaItemList.keySet()) {
            javaItems.put(str, new JavaItem(str, (int) javaItemList.get(str).get("protocol_id")));
        }

        Remapper.addConversions(bedrockItems, javaItems);

        JAVA_ITEMS = javaItems;

        InputStream javaItemStream2 = Toolbox.class.getClassLoader().getResourceAsStream("java/java_blocks.json");
        ObjectMapper javaItemMapper2 = new ObjectMapper();
        Map<String, HashMap> javaItemList2 = new HashMap<>();
        try {
            javaItemList2 = javaItemMapper2.readValue(javaItemStream2, new TypeReference<Map<String, HashMap>>(){});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Map<String, JavaItem> javaItems2 = new HashMap<String, JavaItem>();

        for (String str : javaItemList2.keySet()) {
            javaItems2.put(str, new JavaItem(str, (int) javaItemList2.get(str).get("protocol_id")));
        }

        JAVA_BLOCKS = javaItems2;

        BEDROCK_BLOCKS = m;

        Remapper.addConversions(bedrockItems, javaItems);

        Remapper.addConversions2(m, javaItems2);
    }

    public static final Collection<StartGamePacket.ItemEntry> ITEMS;

    public static final ByteBuf CACHED_PALLETE;

    public static final Map<String, BedrockItem> BEDROCK_ITEMS;
    public static final Map<String, JavaItem> JAVA_ITEMS;

    public static final Map<String, BedrockItem> BEDROCK_BLOCKS;
    public static final Map<String, JavaItem> JAVA_BLOCKS;

    //public static final byte[] EMPTY_CHUNK;
}