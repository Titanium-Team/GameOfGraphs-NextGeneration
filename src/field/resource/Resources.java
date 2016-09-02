package field.resource;

/**
 * Created by 204g03 on 20.06.2016.
 */
public enum Resources implements Resource {

    /**
     * Eine Auflistung aller Resourcen.
     */
    POPULATION {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "People";
        }
    },
    FOOD {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "Food";
        }
    },
    STONE {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "Stone";
        }
    },
    WOOD {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "Wood";
        }
    },
    WHEAT {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "Wheat";
        }
    },
    TREE {
        @Override
        public boolean isMineral() {
            return false;
        }

        @Override
        public String getName() {
            return "Tree";
        }
    },
    IRON {
        @Override
        public boolean isMineral() {
            return true;
        }

        @Override
        public String getName() {
            return "Iron";
        }
    },
    GOLD {
        @Override
        public boolean isMineral() {
            return true;
        }

        @Override
        public String getName() {
            return "Gold";
        }
    };

    /**
     * Gibt ein Array der Resourcen zurück, die als "Specialresource" in Frage kommen.
     * @return
     */
    public static Resource[] getSpecialResources(){

        return new Resource[]{WHEAT, TREE, IRON, GOLD};

    }
}
