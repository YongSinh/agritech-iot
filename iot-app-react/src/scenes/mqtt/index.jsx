import { Box, Typography, useTheme } from "@mui/material";
import { Header } from "../../components";
import { tokens } from "../../theme";
import { useWebSocket } from "../../utils/WebSocketProvider";
import ReactJson from 'react-json-view'
import { DataGrid } from "@mui/x-data-grid";
import { mockDataTeam } from "../../data/mockData";
import {
  AdminPanelSettingsOutlined,
  LockOpenOutlined,
  SecurityOutlined,
} from "@mui/icons-material";


const MQTT = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { data } = useWebSocket(); // Get data from context

    const columns = [
      { field: "id", headerName: "ID" },
      {
        field: "Topic Name",
        headerName: "Name",
        flex: 1,
        cellClassName: "name-column--cell",
      },
      {
        field: "Create By",
        headerName: "Age",
        type: "number",
        headerAlign: "left",
        align: "left",
      },
      {
        field: "access",
        headerName: "Access Level",
        flex: 1,
        renderCell: ({ row: { access } }) => {
          return (
            <Box
              width="120px"
              p={1}
              display="flex"
              alignItems="center"
              justifyContent="center"
              gap={1}
              bgcolor={
                access === "admin"
                  ? colors.greenAccent[600]
                  : colors.greenAccent[700]
              }
              borderRadius={1}
            >
              {access === "admin" && <AdminPanelSettingsOutlined />}
              {access === "manager" && <SecurityOutlined />}
              {access === "user" && <LockOpenOutlined />}
              <Typography textTransform="capitalize">{access}</Typography>
            </Box>
          );
        },
      },
    ];

  return (
    <Box m="20px">
      <Header title="MQTT Topic" subtitle="Managing the MQTT Topic" />
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
          rows={mockDataTeam}
          columns={columns}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 10,
              },
            },
          }}
          checkboxSelection
        />
        {/* <ReactJson src={data} theme="monokai" /> */}
      </Box>
    </Box>
  );
};

export default MQTT;
