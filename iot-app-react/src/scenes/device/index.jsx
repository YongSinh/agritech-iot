import { Box, Typography, useTheme } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import {
  AdminPanelSettingsOutlined,
  LockOpenOutlined,
  SecurityOutlined,
} from "@mui/icons-material";

const Device = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getListDevice() 
  }, []);

  const getListDevice = async () => {
    const result = await request("/iot/api/v1/devices", "GET", null);
    if (result) {
      setDevices(result.data)
      setLoading(false)
      console.log("Data fetched:", result);
    }
  };

  const devicesWithIds = devices.map((row, index) => ({
    id: row.deviceId, // Generate a unique ID
    ...row,
  }));

  const columns = [
    {
      field: "id",
      headerName: "Device  ID"
    },
    {
      field: "name",
      headerName: "Name",
      flex: 1,
      cellClassName: "name-column--cell",
    },
    {
      field: "controller",
      headerName: "Controller",
      flex: 1
    },
    {
      field: "sensors",
      headerName: "Sensors",
      flex: 1
    },
    {
      field: "remark",
      headerName: "Remark",
      flex: 1
    }
  ];
  return (
    <Box m="20px">
      <Header title="DEVICE" subtitle="Managing the iot device" />
      <Box
        mt="40px"
        height="75vh"
        flex={1}
        sx={{
          "& .MuiDataGrid-root": {
            border: "none",
          },
          "& .MuiDataGrid-cell": {
            border: "none",
          },
          "& .name-column--cell": {
            color: colors.greenAccent[300],
          },
          "& .MuiDataGrid-columnHeaders": {
            backgroundColor: colors.blueAccent[700],
            borderBottom: "none",
          },
          "& .MuiDataGrid-virtualScroller": {
            backgroundColor: colors.primary[400],
          },
          "& .MuiDataGrid-footerContainer": {
            borderTop: "none",
            backgroundColor: colors.blueAccent[700],
          },
          "& .MuiCheckbox-root": {
            color: `${colors.greenAccent[200]} !important`,
          },
          "& .MuiDataGrid-iconSeparator": {
            color: colors.primary[100],
          },
        }}
      >
        <DataGrid
          rows={devicesWithIds}
          columns={columns}
          loading={loading}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 10,
              },
            },
          }}
          checkboxSelection
        />
      </Box>
    </Box>
  );
};

export default Device;
