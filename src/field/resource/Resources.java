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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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

        @Override
        public String toString() {
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
