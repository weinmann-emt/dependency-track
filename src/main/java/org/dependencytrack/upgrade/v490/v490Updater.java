/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.dependencytrack.upgrade.v490;

import alpine.common.logging.Logger;
import alpine.persistence.AlpineQueryManager;
import alpine.server.upgrade.AbstractUpgradeItem;
import alpine.server.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class v490Updater extends AbstractUpgradeItem {

    private static final Logger LOGGER = Logger.getLogger(v490Updater.class);

    @Override
    public String getSchemaVersion() {
        return "4.9.0";
    }

    @Override
    public void executeUpgrade(final AlpineQueryManager qm, final Connection connection) throws Exception {
        addLicenseExpressionColumnToComponents(connection);
    }

    private void addLicenseExpressionColumnToComponents(Connection connection) throws Exception {
        // The JDBC type "CLOB" is mapped to the type CLOB for H2, MEDIUMTEXT for MySQL, and TEXT for PostgreSQL and SQL Server.
        LOGGER.info("Adding \"LICENSE_EXPRESSION\" to \"COMPONENTS\"");
        if (DbUtil.isH2()) {
            DbUtil.executeUpdate(connection, "ALTER TABLE \"COMPONENTS\" ADD \"LICENSE_EXPRESSION\" CLOB");
        } else if (DbUtil.isMysql()) {
            DbUtil.executeUpdate(connection, "ALTER TABLE \"COMPONENTS\" ADD \"LICENSE_EXPRESSION\" MEDIUMTEXT");
        } else {
            DbUtil.executeUpdate(connection, "ALTER TABLE \"COMPONENTS\" ADD \"LICENSE_EXPRESSION\" TEXT");
        }
    }

}
