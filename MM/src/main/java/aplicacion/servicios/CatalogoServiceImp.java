package aplicacion.servicios;

import aplicacion.entidades.CatEstatus;
import aplicacion.repositorios.CatEstatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoServiceImp implements CatalogoService {
    @Autowired
    CatEstatusRepository catEstatusRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CatEstatus> getCatEstatus() {
        return modelMapper.map(catEstatusRepo.findAll(), new TypeToken<List<CatEstatus>>() {
        }.getType());
    }
}
