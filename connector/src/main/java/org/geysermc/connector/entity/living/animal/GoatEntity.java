/*
 * Copyright (c) 2019-2021 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.connector.entity.living.animal;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Pose;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import com.nukkitx.math.vector.Vector3f;
import lombok.Getter;
import org.geysermc.connector.entity.EntityDefinition;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.UUID;

public class GoatEntity extends AnimalEntity {
    private static final float LONG_JUMPING_HEIGHT = 1.3f * 0.7f;
    private static final float LONG_JUMPING_WIDTH = 0.9f * 0.7f;

    @Getter
    private boolean isScreamer;

    public GoatEntity(GeyserSession session, long entityId, long geyserId, UUID uuid, EntityDefinition<?> definition, Vector3f position, Vector3f motion, float yaw, float pitch, float headYaw) {
        super(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw);
    }

    public void setScreamer(EntityMetadata<Boolean> entityMetadata) {
        // Metadata not used in Bedrock Edition
        isScreamer = ((BooleanEntityMetadata) entityMetadata).getPrimitiveValue();
    }

    @Override
    protected void setDimensions(Pose pose) {
        if (pose == Pose.LONG_JUMPING) {
            setBoundingBoxWidth(LONG_JUMPING_WIDTH);
            setBoundingBoxHeight(LONG_JUMPING_HEIGHT);
        } else {
            super.setDimensions(pose);
        }
    }
}
