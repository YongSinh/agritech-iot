import { Box, useTheme, Button } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import dayjs from "dayjs";
import Swal from "sweetalert2";
import ModelForm from "./modelForm";

const IntervalSchedule = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deviceIds, setDeviceIds] = useState([]);
  const [open, setOpen] = useState(false);
  const [initialData, setInitialData] = useState(null);
  const [edit, setEdit] = useState(false);

  // Open the form dialog
  const handleClickOpen = () => {
    setOpen(true);
    setEdit(false)
  };

  // Close the form dialog
  const handleClose = () => {
    setOpen(false);
    setEdit(false)
  };

  const handleUpdate = (value) => {
    setInitialData(value);
    setOpen(true);
    setEdit(true)
  };

  const handleSubmit = async (formData) => {
    // console.log(formData)
    // You can now send the formData to your API or perform other actions
    let url = edit ?  "/iot/api/v1/update-control-log" : "/iot/api/v1/create-control-log";
    let method = "post";

    const result = await request(url, method, formData);
    if (result.code === "SUC-000") {
      Swal.fire({
        title: "Success!",
        text: "Your has been saved",
        icon: "success",
        showConfirmButton: false,
        timer: 1500,
      });
      setLoading(false);
      getListIntervalSchedule()
      handleClose(); // Close the dialog after submission
    } else {
      Swal.fire({
        title: "Error!",
        text: result.message,
        icon: "error",
        showConfirmButton: true,
        timer: 3000,
      });
      handleClose(); // Close the dialog after submission
      setLoading(false);
    }
  };

  useEffect(() => {
    getListIntervalSchedule()
    getAllDeviceIds()
  }, []);


  const getAllDeviceIds = async () => {
    const result = await request("/iot/api/v1/device-ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
    }
  };


  const getListIntervalSchedule = async () => {
    const result = await request("/iot/api/v1/control-logs", "GET", null);
    if (result) {
      setData(result.data)
      setLoading(false)
    }
  };
  const columns = [
    { field: "id", headerName: "ID" },
    {
      field: "deviceId",
      headerName: "Device ID",
      flex: 1,
    },
    {
      field: "duration",
      headerName: "Duration",
      flex: 1,
      cellClassName: "name-column--cell",
    },
    {
      field: "status",
      headerName: "Status",
      flex: 1,
    },
    {
      field: "sentBy",
      headerName: "Sent By",
      flex: 1,
    },
    {
      field: "runDatetime",
      headerName: "Run Date time",
      flex: 1,
      renderCell: ({ row: { runDatetime } }) => {
        return (
          <span>{dayjs(runDatetime).format('YYYY-MM-DD h:mm A')}</span>
        );
      },
    },
    {
      headerName: "Action",
      flex: 1,
      renderCell: ({ row }) => {
        return (
          <Button
            color="secondary"
            variant="contained"
            onClick={() => handleUpdate(row)}
          >
            Edit
          </Button>
        );
      },
    }
  ];
  return (
    <Box m="20px">
      <Header title="CONTROL LOGS" subtitle="Managing the IOT Deivce" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add CONTROL LOGS
      </Button>
      <ModelForm
        open={open}
        handleClose={handleClose}
        handleSubmit={handleSubmit} // Pass the submit handler
        colors={colors}
        deviceIds={deviceIds}
        initialData={initialData}
      />
      <Box
        mt="40px"
        height="75vh"
        maxWidth="100%"
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
          rows={data}
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

export default IntervalSchedule;
