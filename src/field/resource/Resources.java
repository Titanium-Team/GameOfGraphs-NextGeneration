package field.resource;

import static game.inventory.resource.ResourceTypes.GOLD;
import static game.inventory.resource.ResourceTypes.IRON;

/**
 * Created by 204g03 on 20.06.2016.
 */
public enum Resources implements Resource {

    POPULATION {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    FOOD {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    STONE {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    WOOD {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    WHEAT {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    HORSES {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    CATTLE {
        @Override
        public boolean isMineral() {
            return false;
        }
    },
    IRON {
        @Override
        public boolean isMineral() {
            return true;
        }
    },
    GOLD {
        @Override
        public boolean isMineral() {
            return true;
        }
    };

    public static Resource[] getSpecialResources(){

        return new Resource[]{WHEAT,HORSES,CATTLE,IRON,GOLD};

    }
}
