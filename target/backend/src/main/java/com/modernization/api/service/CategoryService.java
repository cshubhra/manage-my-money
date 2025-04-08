package com.modernization.api.service;

import com.modernization.api.model.Category;
import com.modernization.api.dto.CategoryDTO;
import com.modernization.api.repository.CategoryRepository;
import com.modernization.api.exception.ResourceNotFoundException;
import com.modernization.api.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getActiveCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return convertToDto(category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category with name " + categoryDTO.getName() + " already exists");
        }
        Category category = convertToEntity(categoryDTO);
        category = categoryRepository.save(category);
        return convertToDto(category);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!category.getName().equalsIgnoreCase(categoryDTO.getName()) &&
            categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category with name " + categoryDTO.getName() + " already exists");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setActive(categoryDTO.isActive());
        
        category = categoryRepository.save(category);
        return convertToDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }

    private CategoryDTO convertToDto(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }
}