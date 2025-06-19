import { Box, useTheme } from "@mui/material";
import { Header } from "../../components";
import { tokens } from "../../theme";
import { useWebSocket } from "../../utils/WebSocketProvider";
import ReactJson from 'react-json-view'


const Team = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { data } = useWebSocket(); // Get data from context

  return (
    <Box m="20px">
      <Header title="TEAM" subtitle="Managing the Team Members" />
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
        <ReactJson src={data} theme="monokai" />
      </Box>
    </Box>
  );
};

export default Team;
